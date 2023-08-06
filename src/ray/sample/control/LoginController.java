package ray.sample.control;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import ray.sample.model.User;
import ray.util.hibernate.DBManager;

@ManagedBean(name = "LoginController")
@ViewScoped
public class LoginController {

	String userName;
	String passWord;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getpassWord() {
		return passWord;
	}

	public void setpassWord(String passWord) {
		this.passWord = passWord;
	}

	public String athentication() throws IOException {

		String hql = String.format("From User f Where f.name= '%s' and f.count='%s' ", userName, passWord);
		@SuppressWarnings("unchecked")
		List<User> f = (List<User>) DBManager.find(hql);
		if (!f.isEmpty()) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("user", f.get(0));
			return "main";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "username or password is incorrect", null));
			return null;
		}
	}
}