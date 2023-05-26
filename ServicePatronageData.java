/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.ServicePatronageView;

/**
 *
 * @author mis12
 */
@ManagedBean
@SessionScoped
public class ServicePatronageData implements Serializable {

    /**
     * Creates a new instance of ServicePatronageData
     */
    public ServicePatronageData() {
    }

    /*
     * properties
     */
    private CustomEntityManagerFactory customEntityManagerFactory;

    private List<ServicePatronageAutoCompleteForm> servicePatronageForm;
    private ServicePatronageAutoCompleteForm servicePatronageCompleteForm;
    private ServicePatronageView servicePatronageView;
    private Integer index;
    private String scAcctno;
    private String lastName;
    private String firstName;
    private Date birthdate;

    /*
     * getter setter
     */
    public CustomEntityManagerFactory getCustomEntityManagerFactory() {
        return customEntityManagerFactory == null ? customEntityManagerFactory = new CustomEntityManagerFactory() : customEntityManagerFactory;
    }

    public void setCustomEntityManagerFactory(CustomEntityManagerFactory customEntityManagerFactory) {
        this.customEntityManagerFactory = customEntityManagerFactory;
    }

    public List<ServicePatronageAutoCompleteForm> getServicePatronageForm() {
        return servicePatronageForm == null ? servicePatronageForm = new ArrayList<>() : servicePatronageForm;
    }

    public void setServicePatronageForm(List<ServicePatronageAutoCompleteForm> servicePatronageForm) {
        this.servicePatronageForm = servicePatronageForm;
    }

    public ServicePatronageAutoCompleteForm getServicePatronageCompleteForm() {
        return servicePatronageCompleteForm;
    }

    public void setServicePatronageCompleteForm(ServicePatronageAutoCompleteForm servicePatronageCompleteForm) {
        this.servicePatronageCompleteForm = servicePatronageCompleteForm;
    }

    public ServicePatronageView getServicePatronageView() {
        return servicePatronageView;
    }

    public void setServicePatronageView(ServicePatronageView servicePatronageView) {
        this.servicePatronageView = servicePatronageView;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getScAcctno() {
        return scAcctno;
    }

    public void setScAcctno(String scAcctno) {
        this.scAcctno = scAcctno;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    /*
     * methods
     */
    public void init() {
        if (FacesContext.getCurrentInstance().isPostback() == false) {
            beanclear();
        }
    }

    public void beanclear() {
        setServicePatronageCompleteForm(null);
        setServicePatronageForm(null);
        setIndex(0);

    }

    public void tabCompleteServicePatronageDb() {
        String query;

        setServicePatronageForm(null);

        if (getScAcctno() != null
                || getLastName() != null
                || getFirstName() != null
                || getBirthdate() != null) {

            try {
                query = "SELECT x"
                        + "FROM ServicePatronageView x "
                        + "WHERE x.ScAcctno = x.ScAcctno";
                if (getScAcctno() != null) {
                    query += "AND x.scAcctno = '" + getScAcctno() + "'";
                }
                if (getLastName() != null) {
                    query += "AND x.lastName = '" + getLastName() + "'";
                }
                if (getFirstName() != null) {
                    query += "AND x.firstName = '" + getFirstName() + "'";
                }
                if (getBirthdate() != null) {
                    query += "AND x.birthDate = '" + getBirthdate() + "'";
                }

                query += "ORDER BY x.scAcctno";

                setServicePatronageForm(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(query).getResultList());
            } catch (Exception e){
               
                System.out.println("TitenginangYAan" + e);
                setLastName(null);
                setScAcctno(null);
                setFirstName(null);
                setBirthdate(null);
            }
            setIndex(0);

        }
    }
}
