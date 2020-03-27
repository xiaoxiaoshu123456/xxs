package com.itheima.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Map map = new HashMap<>();

    private void init(){
        User user = new User();
        user.setUsername("zhangsan");
        user.setPassword(passwordEncoder.encode("1234"));
        map.put(user.getUsername(),user);

        User user2 = new User();
        user2.setUsername("lisi");
        user2.setPassword(passwordEncoder.encode("1234"));
        map.put(user2.getUsername(),user2);
    }

    /**
     * 当我们在登陆页面提交数据发送请求时，会执行该方法，并且将登陆用户的用户名作为参数传递进来
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //初始化数据库
        init();

        //假设从数据库中获取用户信息
        User user = (User) map.get(username);

        //创建一个集合，用于设置用户的权限
        List<GrantedAuthority> list = new ArrayList<>();
        /**
         * 给用户进行授权的规则
         *      1、给用户设置角色：关键字ROLE_角色名称，必须全部大写
         *      2、给用户设置crud的权限：没有大小写的规定， 但是没有ROLE_的前缀
         *
         */
       /* list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        list.add(new SimpleGrantedAuthority("add"));*/

       if (username.equals("zhangsan")){
           list.add(new SimpleGrantedAuthority("add"));
           list.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
       }

       if (username.equals("lisi")){
           list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
       }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),list);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode1 = passwordEncoder.encode("1234");
        String encode2 = passwordEncoder.encode("1234");

        System.out.println(encode1);
        System.out.println(encode2);


        boolean flag = passwordEncoder.matches("1234", encode1);
        boolean flag2 = passwordEncoder.matches("1234", encode2);
        System.out.println(flag);
        System.out.println(flag2);

    }
}
