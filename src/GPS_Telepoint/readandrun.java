package GPS_Telepoint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class readandrun {

	/*
	 * ファイル構成：
	 * homepath - teledir - 都道府県ごと
	 *                    - GPS - 日ごとのGPS
	 *
	 */

	//parameter
	public static Double threshold = 100d;

	public static void main(String[] args) throws IOException {

		String homepath = args[0];
		System.out.println("# Hi! Let's start @Homepath: "+homepath);

		// output folder
		File outdir = new File(homepath+"/output/"); outdir.mkdir();
		System.out.println("--- successfully made outputfolder.");

		// telephone ポイントをマップに整理→高速化
		System.out.println("# start reading telepoint file. this might take a while... ");
		File teledir =new File(homepath+"/telepoints/P1B72/");
		HashSet<String> prefs = new HashSet<String>(); prefs.add("11"); prefs.add("12"); prefs.add("13"); prefs.add("14");
		HashMap<String,HashSet<String>> telepoint = readtelepoint.gettelepoints(teledir, prefs);
		System.out.println("--- successfully made telepoint file by mesh. # of mesh: "+telepoint.size());

		// GPS点データに対して、回す
		System.out.println("# start reading GPS files.");
		File GPS = new File(homepath+"/GPS/");
		File[] gps_days = GPS.listFiles();
		Integer allcount = gps_days.length;
		int count = 0;
		for(File g : gps_days) {
			if(g.getName().contains("interpolated")) {
				File outfile = new File(homepath+"/output/"+g.getName().split("\\.")[0]+"_bylanduse.csv");
				runforindiv(g,outfile,telepoint);
				count++;
				System.out.println("--- done calculation for: "+g.getName()+". ("+count+" out of "+allcount+")");
			}
		}
		System.out.println("# Congrats! DONE! Look in "+homepath+"/output/.");
	}

	public static void runforindiv(File in, File out, HashMap<String,HashSet<String>> telepoint) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		int count = 0;
		while((line=br.readLine())!=null) {
			count++;
			if(count==10000) {
				System.out.println("# done "+count+" lines");
			}
			String[] tokens = line.split(",");
			String id = tokens[0];
//			String network = tokens[1];
//			String mode = tokens[2];
//			String total_points = tokens[3];
//			String no_points = tokens[4];
			String timestamp = tokens[4];
			String lat = tokens[5];
			String lon = tokens[6];
//			String node = tokens[8];
//			String linkid = tokens[9];
//			String meshcode = tokens[10];
//			String fac = tokens[11];
			LonLat p = new LonLat(Double.parseDouble(lon),Double.parseDouble(lat));
			String result = GPSmatching.matchGPS(p, telepoint, threshold);
			if(!result.equals("")) {
				bw.write(id+"\t"+timestamp+"\t"+lon+"\t"+lat+"\t"+result); bw.newLine();
			}
		}
		br.close();
		bw.close();
	}

}
