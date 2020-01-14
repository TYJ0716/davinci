
package edp.davinci.controller;


import com.alibaba.fastjson.JSON;
import edp.core.utils.TokenUtils;
import edp.davinci.dto.userDto.UserLoginResult;
import edp.davinci.model.User;
import edp.davinci.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/cas")
public class CasLoginController {
	@Autowired
	private UserService userService;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private Environment environment;

	@Value("${server.protocol}")
	private String protocol;

	@Value("${server.address}")
	private String address;

	@Value("${server.port}")
	private String port;

	@GetMapping(value = "/login")
	public void davinciLogin(HttpServletRequest request, HttpServletResponse response) {
		String email=null;
		if(AssertionHolder.getAssertion()!=null&&AssertionHolder.getAssertion().getPrincipal()!=null){
			email= AssertionHolder.getAssertion().getPrincipal().getName();
		}else{
			log.error("casLogin error");
		}
		User user = userService.getByUsername(email);
		UserLoginResult userLoginResult = new UserLoginResult(user);
		String statistic_open = environment.getProperty("statistic.enable");
		if("true".equalsIgnoreCase(statistic_open)){
			userLoginResult.setStatisticOpen(true);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("admin", userLoginResult.getAdmin());
		map.put("avatar",userLoginResult.getAvatar());
		map.put("department", userLoginResult.getDepartment());
		map.put("description", userLoginResult.getDescription());
		map.put("email", userLoginResult.getEmail());
		map.put("id", userLoginResult.getId());
		map.put("name", userLoginResult.getName());
		map.put("statisticOpen", "true");
		map.put("username", userLoginResult.getUsername());
		String json = JSON.toJSONString(map);
		String token = tokenUtils.generateToken(user);
		long time = new Date().getTime() + 360000;
		String url=protocol+"://"+address+":"+port+"/#/projects";
		PrintWriter writer=null;
		try {
			response.setCharacterEncoding("utf-8");
			writer = response.getWriter();
			writer.println("<html>");
			writer.println("<head>");
			writer.println("<script>");
			writer.println("  function onload(){");
			writer.println("	window.localStorage.setItem('TOKEN','"+token+"');");
			writer.println("	window.localStorage.setItem('TOKEN_EXPIRE','"+time+"');");
			writer.println("	window.localStorage.setItem('loginUser','"+json+"');");
			writer.println("	window.location.href='"+url+"';");
			writer.println("  }");
			writer.println("</script>");
			writer.println("</head>");
			writer.println("<body onload='onload();'>");
			writer.println("</body>");
			writer.println("</html>");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}



	}
}
