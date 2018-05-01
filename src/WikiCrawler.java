

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WikiCrawler {
	private static final String BASE_URL = "http://web.cs.iastate.edu/~pavan";
	private String seedUrl;
	private int max;
	private List<String> topics;
	private int count;
	private String outputFileName;
	private String outputContent;
	private HashSet<String> visited;
	private HashSet<String> differentEdge;
	public WikiCrawler(String seedUrl, int max, ArrayList<String> topics, String fileName) {

		count = 0;
		this.seedUrl = seedUrl;
		this.max = max;
		this.topics = topics;
		this.outputFileName = fileName;
		outputContent = "";
		differentEdge =new HashSet<String>();

	}

	public void crawl() {
		this.visited = new HashSet<String>();
		Queue<String> urls = new LinkedList<String>();
		urls.add(seedUrl);
		visited.add(seedUrl);
		crawlHelper(urls);
		outputContent = differentEdge.size() + "\n" + outputContent;
		

		writeIntoFile();
	}

	private void writeIntoFile() {
		System.out.println(outputContent);
		File file = new File(outputFileName);
		try {
			if (!file.exists())
				file.createNewFile();
			FileWriter fileWrite = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fileWrite);
			pw.write(outputContent);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void crawlHelper(Queue<String> urls) {

		while (!urls.isEmpty()) {
			if (count >= max)
				return;
			String sourceUrl = urls.poll();
			System.out.println("crawlHelper    " + sourceUrl);
			List<String> neighbors = getNeighbors(sourceUrl);
			for (int i = 0; i< neighbors.size(); ++i) {
			
				String targetUrl = neighbors.get(i);
				outputContent += sourceUrl + " " + targetUrl + "\n";
				System.out.println("crawlHelper    " + targetUrl);
				urls.add(targetUrl);
			}
		}
	}

	private List<String> getNeighbors(String urlStr) {
		List<String> result = new ArrayList<String>();
		List<String> temp = new ArrayList<String>();
		try {
			String response = getResponse(urlStr);
			response = response.replaceAll("&amp;", "&");
			System.out.println(urlStr.toString());
			System.out.println(coverTopic(response));
			if (!coverTopic(response))
				return result;
			
			for (int index = 0; (index = response.indexOf("href=\"")) != -1;) {

				response = response.substring(index + "href=\"".length());
				int end = response.indexOf("\"");
				String nextUrl = response.substring(0, end);
				if ( nextUrl.equals(urlStr) || nextUrl.contains(":")
						|| nextUrl.contains("#") ||( visited.contains(urlStr + nextUrl))) {
					continue;
				}
				String targetResponse = getResponse(nextUrl);
				if(!coverTopic(targetResponse)) {
					System.out.println("nextUrl   " + nextUrl);
					continue;
				}
				
			
				if(!differentEdge.contains(nextUrl)) {
					if(differentEdge.size() >= max) break;
					differentEdge.add(nextUrl);
				}
				temp.add(nextUrl);
				visited.add( urlStr + nextUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (String str : temp) {
			System.out.println("neigobor  " +str);
			result.add(str);
			count++;
			if(count % 25 == 0)
			try {
				Thread.sleep(3000);
				System.out.println("I'm sleep now");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return result;
	}

	private boolean coverTopic(String response) {
		for (String topic : topics) {
			if (response.indexOf(topic) == -1)
				return false;
		}
		return true;
	}
	private String getResponse(String urlStr) {
		String response = "";
		try {
			URL url = new URL(BASE_URL +  urlStr);
			
			
			URLConnection uc = url.openConnection();
			 BufferedReader br = new BufferedReader(new InputStreamReader(
	                 uc.getInputStream()));

			
			for (String line; (line = br.readLine()) != null; response += line);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
