package GPS_Telepoint;

import java.util.HashMap;
import java.util.HashSet;

import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.Mesh;

public class GPSmatching {

	public static String matchGPS(LonLat p, HashMap<String,HashSet<String>> mesh_tatemono, Double threshold) {
		HashSet<String> meshcodes = meshcodes(p);
		HashSet<String> results = new HashSet<String>();
		for(String meshcode : meshcodes) {
			if(mesh_tatemono.containsKey(meshcode)) {
				HashSet<String> set = mesh_tatemono.get(meshcode);
				for(String r : set) {
					String tokens[] = r.split(",");
					String lon = tokens[3]; String lat = tokens[4]; LonLat point = new LonLat(Double.parseDouble(lon),Double.parseDouble(lat));
					// GPS点の空間誤差をパラメタとしてセット
					if(point.distance(p)<threshold) {
						results.add(r);
					} } } }

		if(results.isEmpty()) {
			return "";
		}
		else {
			String resline = "";
			int count = 0;
			for(String r : results) {
				if(count==0) {
					resline = r;
					count++;
				}
				else{
					resline = resline + "\t" + r;
				}
			}
			return resline;
		}
	}


	public static HashSet<String> meshcodes(LonLat p){
		double lon_m = 0.011d;
		double lat_m = 0.009d;
		HashSet<String> res = new HashSet<String>();
		String code = new Mesh(4,p.getLon(),p.getLat()).getCode();
		String codeu = new Mesh(4,p.getLon(),p.getLat()+lat_m*2).getCode();
		String coded = new Mesh(4,p.getLon(),p.getLat()-lat_m*2).getCode();
		String codel = new Mesh(4,p.getLon()-lon_m*2,p.getLat()).getCode();
		String coder = new Mesh(4,p.getLon()+lon_m*2,p.getLat()).getCode();
		String codeur = new Mesh(4,p.getLon()+lon_m*2,p.getLat()+lat_m*2).getCode();
		String codedr = new Mesh(4,p.getLon()+lon_m*2,p.getLat()-lat_m*2).getCode();
		String codeul = new Mesh(4,p.getLon()-lon_m*2,p.getLat()+lat_m*2).getCode();
		String codedl = new Mesh(4,p.getLon()-lon_m*2,p.getLat()-lat_m*2).getCode();
		res.add(code);
		res.add(codeu); res.add(coded); res.add(codel); res.add(coder);
		res.add(codeur); res.add(codedr); res.add(codeul); res.add(codedl);
		return res;
	}

}
