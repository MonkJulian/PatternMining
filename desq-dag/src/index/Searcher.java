package index;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.FixedBitSet;


public class Searcher {

	IndexSearcher searcher;
	
	final FixedBitSet bits;
	
	public Searcher(String indexDir) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		IndexReader iReader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(iReader);
		
		bits = new FixedBitSet(iReader.maxDoc());
	}
	
	public void search(Query query) throws IOException {
		
		searcher.search(query, new Collector() {

			@Override
			public LeafCollector getLeafCollector(LeafReaderContext context) throws IOException {
				final int docBase = context.docBase;
				return new LeafCollector() {

					@Override
					public void collect(int doc) throws IOException {
						bits.set(docBase + doc);
					}

					@Override
					public void setScorer(Scorer scorer) throws IOException {
						// ignore
					}
					
				};
			}

			@Override
			public boolean needsScores() {
				return false;
			}
	
		});
	}
	
	public FixedBitSet getBits() {
		return bits;
	}
	
	
}
