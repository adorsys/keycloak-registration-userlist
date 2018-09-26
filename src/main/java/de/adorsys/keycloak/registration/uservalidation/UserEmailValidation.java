package de.adorsys.keycloak.registration.uservalidation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormActionFactory;
import org.keycloak.authentication.FormContext;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.authentication.forms.RegistrationPage;

import org.keycloak.events.Details;

import org.keycloak.forms.login.LoginFormsProvider;

import org.keycloak.models.utils.FormMessage;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;

import org.keycloak.services.validation.Validation;

public class UserEmailValidation implements FormAction, FormActionFactory{

	private static final String PROVIDER_ID = "registration-user-mail-validation";
	private static final String EMAIL_NOT_ALLOWED = "This e-mail address can't be used for registration";
	
	private static final String EMAIL_READ_FILE = "Problems to read the File with the valid e-mails adress for the user";
	
	private static final String EMAIL_CLOSE_FILE = "Problems to close the File with the valid e-mails adress for the user";

	
	private List<String> allowed_emails = new ArrayList<String>();
	private List<FormMessage> errors = new ArrayList<>();
	
	@Override
	public void validate(ValidationContext context) {
		 	
		
			MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
	       
	        context.getEvent().detail(Details.REGISTER_METHOD, "form");

	        String email = formData.getFirst(Validation.FIELD_EMAIL).trim();
	        context.getEvent().detail(Details.EMAIL, email);
	        
	        
	        boolean isEmailAllowed = false;
	      
	        for(String allowed_email: allowed_emails ) {
	           
	        	if( allowed_email.equals(email)) {
	        		isEmailAllowed = true;
	            	break;
	        	} 
	        }
	       
	        if (! isEmailAllowed) {
	        	context.error(EMAIL_NOT_ALLOWED);
	        	errors.add(new FormMessage(RegistrationPage.FIELD_EMAIL, EMAIL_NOT_ALLOWED));
	        	context.validationError(formData, errors);
	        	return;
            } else {
           
            	context.success();
            }	

	}

	

	@Override
	public void init(Scope arg0) {
		readUsersEmailsAddressFile();
	}
	
	public void readUsersEmailsAddressFile () {
		

        BufferedReader bufferedReader = null; 
       
        //String filePath ="/Users/adro/documents/User_EMails.txt"; 
        String filePath = System.getenv("EMAIL_WHITE_LIST");
       
        File file = new File(filePath); 
        
        try { 
            bufferedReader = new BufferedReader(new FileReader(file)); 
            String line; 
        
            while (null != (line = bufferedReader.readLine())) { 
           
            	allowed_emails.add(line.trim());
            }
           
        } catch (IOException e) { 
        	errors.add(new FormMessage(RegistrationPage.FIELD_EMAIL, EMAIL_READ_FILE));
      	  	e.printStackTrace(); 
        	
        } finally { 
          if (null != bufferedReader) { 
            try { 
              bufferedReader.close(); 
            } catch (IOException e) { 
            	errors.add(new FormMessage(RegistrationPage.FIELD_EMAIL, EMAIL_CLOSE_FILE));
                e.printStackTrace(); 
            } 
          } 
        } 
		
	}

	

	@Override
	public String getDisplayType() {
		return "Registration User Email Allowed Vaidation";
	}
	
	
	@Override
	public String getHelpText() {
		return "Validates the user email adress of the user in validation phase.  In success phase, this will create the user in the database.";

	}


	 @Override
	    public void buildPage(FormContext context, LoginFormsProvider form) {
	        // complete
	    }

	    @Override
	    public boolean requiresUser() {
	        return false;
	    }

	    @Override
	    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
	        return true;
	    }

	    @Override
	    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

	    }

	    @Override
	    public boolean isUserSetupAllowed() {
	        return false;
	    }


	    @Override
	    public void close() {

	    }

	  

	    @Override
	    public String getReferenceCategory() {
	        return null;
	    }

	    @Override
	    public boolean isConfigurable() {
	        return false;
	    }

	    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
	            AuthenticationExecutionModel.Requirement.REQUIRED,
	            AuthenticationExecutionModel.Requirement.DISABLED
	    };
	    @Override
	    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
	        return REQUIREMENT_CHOICES;
	    }
	    @Override
	    public FormAction create(KeycloakSession session) {
	        return this;
	    }

	  

	    @Override
	    public void postInit(KeycloakSessionFactory factory) {

	    }

	    @Override
	    public String getId() {
	        return PROVIDER_ID;
	    }



		@Override
		public List<ProviderConfigProperty> getConfigProperties() {
		
			return null;
		}



		@Override
		public void success(FormContext context) {
			
		}

}
