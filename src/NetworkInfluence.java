

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

public class NetworkInfluence {
	public HashMap<String, Vertice> vertices;

	public NetworkInfluence(String graphData) {
		vertices = GraphUntility.DataToGraph(graphData);
		GraphUntility.findDistance(vertices);
	}

	public int distance(String v, String u) {
		Vertice Vv = vertices.get(v);
		Vertice Vu = vertices.get(u);
		if(Vv == null || Vu == null) return 0;
		return Vv.distance.get(Vu) == null ? -1 : Vv.distance.get(Vu);
	}

	public int distance(ArrayList<String> s, String v) {
		int minDistance = Integer.MAX_VALUE;
		for (String vertice : s) {
			if(distance(vertice, v) == -1) continue;
			minDistance = Integer.min(distance(vertice, v), minDistance);
		}
		return  minDistance;
	}

	public float influence(String u) {
		Vertice Vu = vertices.get(u);
		float influence = 0;
		int i = 1;
		for (Entry<Vertice, Integer> entry : Vu.distance.entrySet()) {

			influence += (float) Math.pow(0.5, distance(u, entry.getKey().url ));
		}
		
		return influence;
	}

	public float influence(ArrayList<String> s) {
		float influence = 0;
		for (Entry<String, Vertice> entry : vertices.entrySet()) {

			influence += (float) Math.pow(0.5,distance(s, entry.getKey()));
		}
		return influence;

	}

	public ArrayList<String> mostInfluentialDegree(int k) {
		ArrayList<String> result = new ArrayList<String>();
		PriorityQueue<Vertice> pq = new PriorityQueue<Vertice>(new Comparator<Vertice>() {
			public int compare(Vertice o1, Vertice o2) {
				int diff =  o2.neigbors.size() - o1.neigbors.size();
				if(diff != 0) return diff;
				else return o1.url.compareTo(o2.url);
			}
		});
		for (Map.Entry<String, Vertice> entry : vertices.entrySet()) {
			pq.add(entry.getValue());
		}
		for (int i = 0; i < k; ++i) {
			String url = pq.poll().url;
			result.add(url);
			System.out.println(url + " " + vertices.get(url).neigbors.size());
		}
		return result;
	}

	public ArrayList<String> mostInfluentialModular(int k) {
		ArrayList<String> result = new ArrayList<String>();
		PriorityQueue<Vertice> pq = new PriorityQueue<Vertice>(new Comparator<Vertice>() {
			public int compare(Vertice o1, Vertice o2) {
				int diff =  (int) (influence(o2.url) - influence(o1.url));
				if(diff != 0) return diff;
				else return o1.url.compareTo(o2.url);
			}
		});
		for (Map.Entry<String, Vertice> entry : vertices.entrySet()) {
			pq.add(entry.getValue());
		}
		for (int i = 0; i < k; ++i) {
			String url = pq.poll().url;
			result.add(url);
			System.out.println(url + " " + vertices.get(url).neigbors.size());
		}
		return result;
	}

	public ArrayList<String> mostInfluentialSubModular(int k) {
		ArrayList<String> result = mostInfluentialDegree(1);
		HashSet<String> inList = new HashSet<String>();
		inList.add(result.get(0));
		String maxV = null;
		for(int i = 0;  i< k - 1; ++i) {
			float maxInfluence = Integer.MIN_VALUE;
			for (Map.Entry<String, Vertice> entry : vertices.entrySet()) {

				if(inList.contains(entry.getKey())) continue;
				result.add(entry.getKey());
				System.out.println(entry.getKey() +" ^^^ " + influence(result));
				if( maxInfluence < influence(result)) {
					
					maxInfluence = influence(result);
					maxV  = entry.getKey();
				}
				result.remove(result.size() - 1);
			}
			inList.add(maxV);
			result.add(maxV);
		}

		return result;
	}
	private List<String> shortestPathList;
	private boolean shortestPathListFinished;
	public List<String> shortestPath(String src, String des){
		shortestPathListFinished = false;
		List<String> list = new LinkedList<String>();
		shortestPathHelper(list, src, des, 0, distance(src, des), new HashSet<String>());
		
		return shortestPathList;
	}
	private void shortestPathHelper(List<String> list, String current, String des, int deep, int distance, HashSet<String> isVisited){
		System.out.println("@@"+list.size());
		if(shortestPathListFinished || isVisited.contains(current)) return;
		System.out.println("@@@"+list.size());
		list.add(current);
		isVisited.add(current);
		if(current.equals(des) && deep == distance) {
			System.out.println("made it");
			shortestPathList = new ArrayList<String>(list);
			shortestPathListFinished  = true;
			return;
		}
		for(Vertice neigbor: vertices.get(current).neigbors) {
			shortestPathHelper(list, neigbor.url, des, deep + 1, distance, isVisited);
		}

		isVisited.remove(current);
		list.remove(list.size() - 1);
	}
}
