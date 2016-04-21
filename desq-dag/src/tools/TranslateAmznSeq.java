package tools;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class TranslateAmznSeq {

	public Object2ObjectOpenHashMap<String, String> idToName = new Object2ObjectOpenHashMap<String, String>();

	public void processTitles(String file) throws IOException {
		FileInputStream fstream;
		fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		while ((strLine = br.readLine()) != null) {
			if (!strLine.isEmpty()) {
				String[] tokens = strLine.split("\\s* \\s*");
				if (tokens.length > 1) {
					String productId = tokens[0].trim();

					String productName = tokens[1].trim();
					for (int i = 2; i < tokens.length; i++) {
						productName = productName + "_" + tokens[i].trim();
					}
					// System.out.println(productId + " : " + productName);
					idToName.put(productId, productName);
				}
			}
		}
		br.close();
	}

	public void translate(String inputFile, String outputFile) throws IOException {

		File outFile = new File(outputFile);

		OutputStream fstreamOutput = new FileOutputStream(outFile);
		// Get the object of DataOutputStream
		DataOutputStream out = new DataOutputStream(fstreamOutput);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));

		FileInputStream fstream;
		fstream = new FileInputStream(inputFile);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String inputLine;
		String outputLine = null;
		while ((inputLine = br.readLine()) != null) {
			if (!inputLine.isEmpty()) {
				String[] tokens = inputLine.split("\t");
				// System.out.println(tokens);
				outputLine = tokens[0].trim() + "\t";
				String[] sequence = tokens[1].split("\\s* \\s*");
				// System.out.println(sequence);
				String productName = idToName.get(sequence[0].trim());
				if (productName == null)
					productName = sequence[0].trim();

				outputLine = outputLine + productName;

				for (int i = 1; i < sequence.length; ++i) {
					outputLine = outputLine + " ";
					productName = idToName.get(sequence[i].trim());
					if (productName == null)
						productName = sequence[i].trim();
					outputLine = outputLine + productName;
				}
				// System.out.println(outputLine);

				bw.write(outputLine + "\n");

			}
		}
		br.close();
		bw.close();

	}

	public void processDir(File dir) throws IOException {

		File listFile[] = dir.listFiles();
		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isDirectory()) {
					processDir(listFile[i]);
				} else {
					String inputFile = listFile[i].getAbsolutePath();
					String outputFile = inputFile + ".translated";
					translate(inputFile, outputFile);
				}
			}
		}
	}

	// <path/to/titles.txt> <input dir>
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*
		 * String titles = "/home/kbeedkar/Data/amazon-reviews/titles.txt";
		 * String inputFile = "/home/kbeedkar/vldb2015/amzn-output/q1-100";
		 * String outputFile = inputFile + ".translated";
		 */

		String titles = args[0];
		String inputDir = args[1];

		TranslateAmznSeq tas = new TranslateAmznSeq();
		tas.processTitles(titles);
		// tas.translate(inputFile, outputFile);

		tas.processDir(new File(inputDir));

	}

}
