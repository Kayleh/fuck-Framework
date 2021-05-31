package demo;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 用户列表
     *
     * @return
     */
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public View getUserList() {
        List<User> userList = userService.getAllUser();
        return new View("index.jsp").addModel("userList", userList);
    }
}
