package index;



import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import utils.Dictionary;

public class Indexer {
	
	private IndexWriter writer;
	private Dictionary dictionary = Dictionary.getInstance();
	
	public Indexer(String indexDirPath) throws IOException {
		// directory containing indexes
		Directory indexDir = FSDirectory.open(Paths.get(indexDirPath));
		
		// create the indexer
		writer = new IndexWriter(indexDir, new IndexWriterConfig(new SimpleAnalyzer()));
	}
	
	public void close() throws IOException {
		writer.close();
	}
	
	private Document getDocument(String inputSequence) {
		int[] copy = new int[inputSequence.length()];
		String[] tokens = inputSequence.split("\\s* \\s*");
		int copySize = 0;
		for(String token: tokens) {
			copy[copySize++] = Integer.parseInt(token);
		}
		return getDocument(copy);
	}
	
	private Document getDocument(int[] inputSequence) {
		Document doc = new Document();
		Int2IntOpenHashMap itemCounts = new Int2IntOpenHashMap();
		itemCounts.defaultReturnValue(0);
		
		for(int item : inputSequence) {
			for(int id : dictionary.getAncestors(item)) {
				itemCounts.addTo(id, +1);
			}
		}
		
		for(int key : itemCounts.keySet()) {
			doc.add(new IntField("itemId_" + key, itemCounts.get(key), Field.Store.NO));
		}
		
		return doc;
	}
	
	public void buildIndex(String sequenceFile) throws IOException {
		System.out.println("Begin indexing...");
		FileInputStream fstream = new FileInputStream(sequenceFile);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		int sId = 0;
		while ((line = br.readLine()) != null) {
			if (!line.isEmpty()) {
				writer.addDocument(getDocument(line));
				sId++;
				
				if(sId % 10000 == 0) {
					System.out.println("Indexed " + sId + " sequences; commiting now");
					writer.commit();
				}
			}
		}
		br.close();
		System.out.println("final commit");
		writer.commit();
		System.out.println("merging segments");
		writer.forceMerge(1, true);
		
		System.out.println("End indexing...");
		writer.close();
	}

}
