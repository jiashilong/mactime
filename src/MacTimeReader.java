import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;


public class MacTimeReader {

	private Map<String, ArrayList<String>> macTimeMap = new  LinkedHashMap<String, ArrayList<String>>();
	
	public void read(String path) {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(path));
			String line = null;
			String[] words = null;
			String time = null;
			String mac = null;
			
			while((line=br.readLine()) != null) {
				if(line.contains("added mac")) {
					words = line.split("\\s+");
					if(words.length != 13) {continue;}
					
					time = words[0] + " " + words[1] + " " + words[2];
					mac = words[10].replace(",", "");
					
					ArrayList<String> timeList = macTimeMap.get(mac);
					if(null == timeList) {
						timeList = new ArrayList<String>();
					}
					timeList.add(time);
					
					macTimeMap.put(mac, timeList);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) {br.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void write(String path) {
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(path);
			for(String mac : macTimeMap.keySet()) {
				ArrayList<String> timeList = macTimeMap.get(mac);
				for(String time : timeList) {
					System.out.print(mac + "," + time + "\n");
					fw.write(mac + "," + time + "\n");
					fw.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fw != null) {fw.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeEx(String path) {
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(path);
			for(String mac : macTimeMap.keySet()) {
				ArrayList<String> timeList = macTimeMap.get(mac);
				StringBuffer sb = new StringBuffer();
				for(int i=0; i<timeList.size()-1; i++) {
					sb.append(timeList.get(i)+",");
				}
				sb.append(timeList.get(timeList.size()-1));
				String timeLine = sb.toString();
				System.out.println(mac + "," + timeLine);
				fw.write(mac + "," + timeLine + "\n");
				fw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fw != null) {fw.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		MacTimeReader reader = new MacTimeReader();
		reader.read("syslog");
		reader.write("syslog_mac_time.csv");
		reader.writeEx("syslog_mac_time_ex.csv");
	}

}
