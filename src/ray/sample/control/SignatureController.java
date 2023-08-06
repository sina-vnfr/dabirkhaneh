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

@ManagedBean(name = "SignatureController")
@ViewScoped
public class SignatureController {
	private String topic;
	private String sender;
	private List<User> employee;
	private List<Letter> letter;
	private ExternalContext externalContext;
	private Map<String, Object> sessionMap;

	@PostConstruct
	public void init() {
		externalContext = FacesContext.getCurrentInstance().getExternalContext();
		sessionMap = externalContext.getSessionMap();
		User user = (User) sessionMap.get("user");
		Long receiver = user.getId();
		String hql = String.format("From Letter letter Where letter.receiver= '%x' ", receiver);
		@SuppressWarnings("unchecked")
		List<Letter> letter = (List<Letter>) DBManager.find(hql);
		setLetter(letter);

		String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		ExternalContext externalContextPages = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMapPages = externalContextPages.getSessionMap();
		sessionMapPages.put("previos", viewId);

	}

	public List<User> getEmployee() {
		return employee;
	}

	public void setEmployee(List<User> employee) {
		this.employee = employee;
	}

	public List<Letter> getLetter() {
		return letter;
	}

	public void setLetter(List<Letter> letter) {
		this.letter = letter;
	}

	public ExternalContext getExternalContext() {
		return externalContext;
	}

	public void setExternalContext(ExternalContext externalContext) {
		this.externalContext = externalContext;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getBack() {
		return "main";
	}

	public String checkLetter(Long letterId) {
		if (letterId != null && letterId > 0) {
			externalContext = FacesContext.getCurrentInstance().getExternalContext();
			sessionMap = externalContext.getSessionMap();
			sessionMap.put("letterId", letterId);
			return "writeLetter";
		}
		return null;
	}
}
