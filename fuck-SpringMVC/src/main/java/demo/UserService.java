package demo;

import java.util.List;

@Service
public class UserService implements IUserService {
    /**
     * 获取所有用户
     */
    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "Tom", 22));
        userList.add(new User(2, "Alic", 12));
        userList.add(new User(3, "Bob", 32));
        return userList;
    }
}
