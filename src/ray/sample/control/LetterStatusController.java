package ray.sample.control;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import ray.sample.model.Letter;
import ray.sample.model.User;
import ray.util.hibernate.DBManager;

@ManagedBean(name = "LetterStatusController")
@ViewScoped
public class LetterStatusController {

	private String sender;
	private List<Letter> letter;
	private Map<String, Object> sessionMap;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		User user = (User) sessionMap.get("user");
		String hql = String.format("From Letter letter Where letter.sender= '%s' ", user.getName());
		letter = (List<Letter>) DBManager.find(hql);
		setLetter(letter);
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public List<Letter> getLetter() {
		return letter;
	}

	public void setLetter(List<Letter> letter) {
		this.letter = letter;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getBack() {
		return "main";
	}
}
