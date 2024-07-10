package processor;

import db.Database;
import model.User;
import webserver.Request;

import java.util.HashMap;

public class UserProcessor {
    public void createUser(Request request) {
        HashMap<String, String> userData = request.parseBody();
        User user = User.from(userData);
        System.out.println(user);
        Database.addUser(user);
    }

    public boolean login(Request request) {
        HashMap<String, String> userData = request.parseBody();
        String userId = userData.get("userId");
        String password = userData.get("password");
        User user = Database.findUserById(userId);

        if (user != null) {
            if (password.equals(user.getPassword())) {
                // 로그인 성공
                return true;
            } else {
                // 로그인 실패. 패스워드 불일치
                return false;
            }
        } else {
            // 로그인 실패. 존재하지 않는 사용자
            return false;
        }
    }
}
