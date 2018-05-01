

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class GraphUntility {
	public static HashMap<String, Vertice> DataToGraph(String fileName) {
		HashMap<String, Vertice> result = new HashMap<String, Vertice>();
		try {
			Reader graphDatafile;
			graphDatafile = new FileReader(new File(fileName));
			BufferedReader br = new BufferedReader(graphDatafile);
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				
				int index = line.indexOf(" ");
				String source = line.substring(0, index);
				String destination = line.substring(index + 1 );
				System.out.println(source +"**"+ destination);
				
				Vertice sourceVertice = result.getOrDefault(source, new Vertice(source));
				Vertice destinationVertice = result.getOrDefault(destination, new Vertice(destination));
				result.put(source, sourceVertice);
				result.put(destination, destinationVertice);
				sourceVertice.neigbors.add(destinationVertice);
				destinationVertice.neigbors.add(sourceVertice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static void findDistance(Map<String, Vertice> vertices) {
		for (Map.Entry<String, Vertice> entry : vertices.entrySet()) {
			findDistanceHelper(vertices, entry.getKey(), new HashSet<String>());
			// System.out.println(entry.getValue().distance.get(vertices.get("/wiki/Iowa_State_Myths_and_Legends")));
		}

	}

	private static void findDistanceHelper(Map<String, Vertice> vertices, String startVertice,
			HashSet<String> isVisited) {
		Queue<String> queue = new LinkedList<String>();
		queue.add(startVertice);
		queue.add(null);
		int distance = 1;
		Vertice Vsource = vertices.get(startVertice);
		Vsource.distance.put(Vsource, 0);
		isVisited.add(startVertice);
		while (!queue.isEmpty()) {
			
			Vertice v = vertices.get(queue.poll());
			if(v == null) {
				distance ++;
				if(queue.isEmpty()) break;
				v = vertices.get(queue.poll());
				queue.add(null);
				
			}

			for (Vertice neigbor : v.neigbors) {
				if (isVisited.contains(neigbor.url))
					continue;
				if(Vsource.url.equals("A")) {
					 System.out.println(v.url + "&&& " + neigbor.url + " " +
							 distance);
				 }
				isVisited.add(neigbor.url);
				queue.add(neigbor.url);
				if(!Vsource.distance.containsKey(neigbor)) Vsource.distance.put(neigbor, distance);
				 
			}
			
		}
	}
}
