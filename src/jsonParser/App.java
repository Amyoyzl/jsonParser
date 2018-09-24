package jsonParser;

import java.util.HashMap;

public class App {
	
	public static void main(String[] args) {
		
		User u = new User(1,"bob","139");

		String str = new Parser().toJson(u);
		System.out.println(str);
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("code", 1);
		map.put("msg","ok");
		String text = new Parser().toJson(map);
		System.out.println(text);
		
		String json = "{id:4,name:bob,phone:183423434}";
		User user = (User) new Parser().toObject(json, User.class);
		System.out.println(user);
	}

}
