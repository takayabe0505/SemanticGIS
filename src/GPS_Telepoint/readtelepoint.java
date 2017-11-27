package GPS_Telepoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.Mesh;

public class readtelepoint {

	// ここでのインプットは、都道府県ごとのフォルダが全部入った親フォルダ
	public static HashMap<String,HashSet<String>> gettelepoints(File dir, HashSet<String> targetprefs) throws IOException {

		// 整理結果が入るデータの箱
		HashMap<String,HashSet<String>> meshcode_tatemono = new HashMap<String,HashSet<String>>();

		File[] telepointfiles = dir.listFiles();
		// 各都道府県のフォルダに対して回す
		for(File f : telepointfiles) {
			String filename = f.getName().split("\\.")[0]; // 都道府県コードが返ってくる
			if(targetprefs.contains(filename)) { // おそらく関東圏だけだから絞れるように
				File[] cityfiles = f.listFiles();
				for(File c : cityfiles) {
					readfile(c, meshcode_tatemono);
				}
				System.out.println("--- Done prefecture number: "+filename);
			}
//			else {
//				System.out.println("--- ERROR: there is no "+filename);
//			}
		}
		return meshcode_tatemono;
	}

	// シェープを使った空間検索は時間かかるからメッシュ単位でまとめる。
	// 関東で~30000メッシュ程度だからイケるはず。
	public static void readfile(File in, HashMap<String,HashSet<String>> meshcode_tatemono) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null) {
			String tokens[] = line.split(",");
			String name = tokens[0];
			String code = tokens[10];
			String accuracy = tokens[19];
			String lon = tokens[20];
			String lat = tokens[21];
			if((accuracy.equals("70"))||(accuracy.equals("80"))) {
				LonLat p = new LonLat(Double.parseDouble(lon),Double.parseDouble(lat));
				Mesh mesh = new Mesh(4,p.getLon(),p.getLat());
				String meshcode = mesh.getCode();
				String res = name+","+code+","+accuracy+","+lon+","+lat;
				if(meshcode_tatemono.containsKey(meshcode)) {
					meshcode_tatemono.get(meshcode).add(res);
				}
				else {
					HashSet<String> temp = new HashSet<String>();
					temp.add(res);
					meshcode_tatemono.put(meshcode, temp);
				}
			}
		}
		br.close();
	}

}
