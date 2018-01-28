import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class utils {

	
	
	public static void installFirm(){
		try {
			Process p = Runtime.getRuntime().exec( "cmd.exe /E:1900 /C firmdl.bat" );
			InputStream is = p.getInputStream();
			BufferedReader b = new BufferedReader(new InputStreamReader(is));
			BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String line = "";
			
			while((line = b.readLine())!= null || (line = error.readLine()) != null){
				System.out.println(line);
			}
			
			
		//	p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		utils.installFirm();

	}

}
