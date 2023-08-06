package ray.sample.control;

import java.util.ArrayList;
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

@ManagedBean(name = "WriterLetterController")
@ViewScoped
public class WriterLetterController {

	private List<String> employee;
	private List<String> selectedEmployee;
	private String topic;
	private String context;
	private Map<String, Object> sessionMap;
	private String periviosPage;

	@PostConstruct
	public void init() {
		employee = new ArrayList<String>();

		String hqlEmployee = String.format("From User tbl_foo");
		@SuppressWarnings("unchecked")
		List<User> employeeObject = (List<User>) DBManager.find(hqlEmployee);

		for (int i = 0; i < employeeObject.size(); i++)
			employee.add(employeeObject.get(i).getName());

		ExternalContext externalContextPages = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContextPages.getSessionMap();
		periviosPage = (String) sessionMap.get("previos");
		sessionMap.remove("previos");

		Long letterId = (Long) sessionMap.get("letterId");
		String hql = String.format("From Letter letter Where letter.id= '%d' ", letterId);
		@SuppressWarnings("unchecked")
		List<Letter> lettersList = (List<Letter>) DBManager.find(hql);

		String hqlContext = String.format("From Letter letter Where letter.context= '%s' ",
				lettersList.get(0).getContext());
		@SuppressWarnings("unchecked")
		List<Letter> lettersListContext = (List<Letter>) DBManager.find(hqlContext);

		if (periviosPage.equals("/signature.xhtml")) {
			topic = lettersList.get(0).getTopic();
			context = lettersList.get(0).getContext();
			selectedEmployee = new ArrayList<String>();
			for (Letter item : lettersListContext) {
				if (item.getReceiver() != null && item.getReceiver() > 0) {
					String userHql = String.format("From User u Where u.id= '%x' ", item.getReceiver());
					@SuppressWarnings("unchecked")
					List<User> user = (List<User>) DBManager.find(userHql);
					selectedEmployee.add(user.get(0).getName());
				}
			}
		}
	}

	public List<String> getEmployee() {
		return employee;
	}

	public void setEmployee(List<String> employee) {
		this.employee = employee;
	}

	public List<String> getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(List<String> selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public void dataSavedButten() throws Exception {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		User user = (User) sessionMap.get("user");

		Letter letter = new Letter();
		letter.setTopic(topic);
		letter.setSender(user.getName());
		letter.setContext(context);

		if (!topic.isEmpty() && !context.isEmpty() && !selectedEmployee.isEmpty()) {
			for (String name : selectedEmployee) {
				String hql = String.format("From User f Where f.name= '%s' ", name);
				@SuppressWarnings("unchecked")
				List<User> f = (List<User>) DBManager.find(hql);
				letter.setReceiver(f.get(0).getId());
				DBManager.save(letter);
			}
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "data has been saved", null));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"your data already stored or you have empty fild", null));
		}
	}

	public Boolean getDisabledElementInputText(String elementId) {

		if (elementId.equals("inputTextarea") && periviosPage.equals("/main.xhtml")) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public Boolean getDisabledElement(String elementId) {

		if (elementId.equals("displayInput") && periviosPage.equals("/main.xhtml")) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	public Boolean getRenderdElement(String elementId) {

		if (elementId.equals("save") && periviosPage.equals("/main.xhtml")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getRenderdSignatureElement(String elementId) {

		if (elementId.equals("accept") && periviosPage.equals("/main.xhtml")) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;

	}

	public Boolean getRenderRejectSignatureElement(String elementId) {

		if (elementId.equals("Reject") && periviosPage.equals("/main.xhtml")) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;

	}

	public void AcceptRejectButten(String elementId) throws Exception {
		ExternalContext externalContextPages = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContextPages.getSessionMap();

		Long letterId = (Long) sessionMap.get("letterId");
		String hql = String.format("From Letter letter Where letter.id= '%d' ", letterId);
		@SuppressWarnings("unchecked")
		List<Letter> lettersList = (List<Letter>) DBManager.find(hql);
		Letter newLetter = lettersList.get(0);

		if (newLetter.getSignature() == null) {
			if (elementId.equals("accept")) {
				newLetter.setSignature(Boolean.TRUE);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "you accept this letter", null));
			}
			if (elementId.equals("Reject")) {
				newLetter.setSignature(Boolean.FALSE);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "you reject this letter", null));
			}
			DBManager.update(newLetter);
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "letter was signed or you dont login", null));
		}
	}

	public String getBack() {
		return "main";
	}
}
