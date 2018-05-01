import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class NetworkInfluence {
	private HashMap<String, Vertice> vertices;
	public NetworkInfluence(String graphData) {
		vertices = GraphUntility.DataToGraph(graphData);
	}
	public int distance(String v, String u) {
		Vertice Vv = vertices.get(v);
		Vertice Vu = vertices.get(u);
		return Vv.distance.get(Vu);
	}
	public int distance (ArrayList<String> s, String v) {
		int minDistance = Integer.MAX_VALUE;
		for(String vertice : s) {
			minDistance = Integer.max(distance(vertice, v), minDistance);
		}
		return minDistance;
	}
	public float influence(String u) {
		Vertice Vu = vertices.get(u);
		float influence = 0;
		for(Entry<Vertice, Integer> entry : Vu.distance.entrySet()) {
			influence +=  (float) Math.pow(0.5, entry.getValue());
		}
		return influence;
	}
	public float  influence(ArrayList<String> s) {
		float influence = 0;
		for(Entry<String, Vertice> entry : vertices.entrySet()) {
			influence += distance(s, entry.getKey());
		}
		return influence;
	
	}
	public ArrayList<String> mostInfluentialDegree(int k){
		ArrayList<String> result = new ArrayList<String>();
		PriorityQueue<Vertice> pq = new PriorityQueue<Vertice>(new Comparator<Vertice>() {

			@Override
			public int compare(Vertice o1, Vertice o2) {
				return o1.neigbors.size() - o2.neigbors.size();
			}
			
		});
		for(Map.Entry<String, Vertice> entry : vertices.entrySet()) {
			pq.add(entry.getValue());
		}
		for(int i = 0; i < k; ++i) {
			result.add(pq.poll().url);
		}
		return result;
	}
	
	public ArrayList<String> mostInfluentialModular(int k){
		ArrayList<String> result = new ArrayList<String>();
		PriorityQueue<Vertice> pq = new PriorityQueue<Vertice>(new Comparator<Vertice>() {

			@Override
			public int compare(Vertice o1, Vertice o2) {
				return (int)(influence(o1.url) - influence(o2.url));
			}
			
		});
		for(Map.Entry<String, Vertice> entry : vertices.entrySet()) {
			pq.add(entry.getValue());
		}
		for(int i = 0; i < k; ++i) {
			result.add(pq.poll().url);
		}
		return result;
	}
	public ArrayList<String> mostInfluentialSubModular(int k){
		ArrayList<String> result = new ArrayList<String>();
		Map<String, Vertice> tempMap = new HashMap<String, Vertice>(vertices);
		for(int i = 0; i < k; ++i) {
			float min = Float.MAX_VALUE;
			Vertice best = null;
			for(Map.Entry<String, Vertice> entry: tempMap.entrySet()) {
				result.add(entry.getValue().url);
				float influenceValue = influence(result);
				if(min > influenceValue){
					min =  influenceValue;
					best = entry.getValue();
				}
				result.remove(result.size() -1);
			}
			result.add(best.url);
		}
		return result;
	}
}
