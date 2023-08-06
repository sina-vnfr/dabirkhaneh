package ray.sample.control;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import ray.sample.model.Letter;
import ray.sample.model.User;
import ray.util.hibernate.DBManager;

@ManagedBean(name = "ArchiveController")
@ViewScoped
public class ArchiveController {
	private String topic;
	private String sender;
	private List<Letter> letter;
	private List<Letter> archiveLetter;
	private List<Letter> selectedLetter;

	public List<Letter> getSelectedLetter() {
		return selectedLetter;
	}

	public void setSelectedLetter(List<Letter> selectedLetter) {
		this.selectedLetter = selectedLetter;
	}

	private ExternalContext externalContext;
	private Map<String, Object> sessionMap;

	@PostConstruct
	public void init() {
		ExternalContext externalContextPages = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContextPages.getSessionMap();
		User user = (User) sessionMap.get("user");

		if (!user.getName().isEmpty()) {
			String hql = String.format(
					"From Letter letter Where (letter.signature= '%d' or letter.signature='%d') and letter.archive is null ",
					1, 0);
			@SuppressWarnings("unchecked")
			List<Letter> letters = (List<Letter>) DBManager.find(hql);
			setLetter(letters);

			String hqlArchive = String.format("From Letter letter Where letter.archive= '%d'", 1);
			@SuppressWarnings("unchecked")
			List<Letter> archiveSavedLetter = (List<Letter>) DBManager.find(hqlArchive);
			setArchiveLetter(archiveSavedLetter);
		}
	}

	public List<Letter> getArchiveLetter() {
		return archiveLetter;
	}

	public void setArchiveLetter(List<Letter> archiveLetter) {
		this.archiveLetter = archiveLetter;
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

	public void saveArchive() throws Exception {

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.FACES_MESSAGES, "letters was archived"));
		for (int i = 0; i < selectedLetter.size(); i++) {
			Letter newLetter = selectedLetter.get(i);
			newLetter.setArchive(Boolean.TRUE);
			DBManager.update(newLetter);
		}
		
	}

	public String getBack() {
		return "main";
	}
}
