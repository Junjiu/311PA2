import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WikiCrawler {
	private static final String BASE_URL = "https://en.wikipedia.org";
	private String seedUrl;
	private int max;
	private List<String> topics;
	private int count;
	private String outputFileName;
	private String outputContent;
	public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, 
			String fileName) {
		
		count = 0;
		this.seedUrl = seedUrl;
		this.max = max;
		this.topics = topics;
		this.outputFileName = fileName;
		outputContent = "";
		
	}
	public void crawl() {
		Queue<String> urls = new LinkedList<String>();
		urls.add(seedUrl);
		crawlHelper(urls);
		outputContent = Integer.toString(count) + "\n" + outputContent;
		//System.out.println(outputContent);
		writeIntoFile();
	}
	private void writeIntoFile() {
		File file = new File(outputFileName);
		try {
			if(!file.exists()) file.createNewFile();
			FileWriter fileWrite = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fileWrite);
			pw.write(outputContent);
			pw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		
	}
	private void crawlHelper(Queue<String> urls ) {
	
		while(!urls.isEmpty()) {
			if(count >= max) return;
			String sourceUrl = urls.poll();
			List<String> neighbors = getNeighbors(sourceUrl);
			for(String targetUrl: neighbors) {
				outputContent += sourceUrl + " " + targetUrl + "\n";
				urls.add(targetUrl);
			}
		}
	}
	private List<String> getNeighbors(String urlStr){
		List<String> result = new ArrayList<String>();
		HashSet<String> temp = new HashSet<String>();
		try {
			URL url = new URL(BASE_URL + urlStr);
			//System.out.println(url.toString());
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String response = "";
			for(String line;(line = br.readLine()) != null; response += line);
			response = response.substring(response.indexOf("<p>"));
			response = response.replaceAll("&amp;","&");
			if(!coverTopic(response)) return result;
			for(int index = 0; (index = response.indexOf("href=\"")) != -1;){
				
				response = response.substring(index + "href=\"".length());
				int end = response.indexOf("\"");
				String nextUrl = response.substring(0, end);
				if(nextUrl.indexOf("/wiki/") != 0 || nextUrl.equals(urlStr)) {
					continue;
				}
				temp.add(nextUrl);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		for(String str: temp) {
			result.add(str);
			count++;
//			if(count % 25 == 0)
//				try {
//					Thread.sleep(3000);
//					System.out.println("I'm sleep now");
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			if(count >= max) return result;
		}
	
		
		return result;
	}
	private boolean coverTopic(String response) {
		for(String topic: topics) {
			if(response.indexOf(topic) == -1) return false;
		}
		return true;
	}
}
