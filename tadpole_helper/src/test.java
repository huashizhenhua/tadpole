public class test {
	public static void main(String[] args) {
		String url = "file:///android_asset/ucgamesdk/ui/production/user/user.html#!user/rapidReg";
		url = url.replaceAll("#!.*", "");
		System.out.println("url = " + url);
	}
}
