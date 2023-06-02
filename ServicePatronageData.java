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
import model.MyServices;
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

    private List<ServicePatronageView> servicePatronageView;
    private List<MyServices> myServices;
    private List<Object[]> servicesType;
    private Integer index;
    private String scAcctno;
    private String lastName;
    private String firstName;
    private String addNewGroup;
    private String orgName;
    private Date birthdate;
    private Date serviceDropdownDate;
    private Integer type;
    private Integer dropDown;


    /*
     * getter setter
     */
    public CustomEntityManagerFactory getCustomEntityManagerFactory() {
        return customEntityManagerFactory == null ? customEntityManagerFactory = new CustomEntityManagerFactory() : customEntityManagerFactory;
    }
//
//    public void setCustomEntityManagerFactory(CustomEntityManagerFactory customEntityManagerFactory) {
//        this.customEntityManagerFactory = customEntityManagerFactory;
//    }

    public List<ServicePatronageView> getServicePatronageView() {
        return servicePatronageView == null ? servicePatronageView = new ArrayList<>() : servicePatronageView;
    }

    public void setServicePatronageView(List<ServicePatronageView> servicePatronageView) {
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<MyServices> getMyServices() {
        return myServices == null ? myServices = new ArrayList<>() : myServices;
    }

    public void setMyServices(List<MyServices> myServices) {
        this.myServices = myServices;
    }

    public Integer getDropDown() {
        return dropDown;
    }

    public void setDropDown(Integer dropDown) {
        this.dropDown = dropDown;
    }

    public List<Object[]> getServicesType() {
        return servicesType == null ? servicesType = new ArrayList<>() : servicesType;
    }

    public void setServicesType(List<Object[]> servicesType) {
        this.servicesType = servicesType;
    }

    public Date getServiceDropdownDate() {
        return serviceDropdownDate;
    }

    public void setServiceDropdownDate(Date serviceDropdownDate) {
        this.serviceDropdownDate = serviceDropdownDate;
    }

    public String getAddNewGroup() {
        return addNewGroup;
    }

    public void setAddNewGroup(String addNewGroup) {
        this.addNewGroup = addNewGroup;
    }
    /*
     * methods
     */

    public void tabConvert() {

    }

    public void init() {
        if (FacesContext.getCurrentInstance().isPostback() == false) {
            beanclear();
        }
    }

    public void beanclear() {
        setServicePatronageView(null);
        setIndex(0);
        setLastName(null);
        setFirstName(null);
        setScAcctno(null);
        setBirthdate(null);

    }

    public void tabCompleteServicePatronageDb() {
        String query;

        setServicePatronageView(null);

        System.out.println("WAWAW");

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
                setServicePatronageView(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(query).getResultList());
            } catch (Exception e) {

                System.out.println("ServicePatronageData" + e);
                setLastName(null);
                setScAcctno(null);
                setFirstName(null);
                setBirthdate(null);
            }
            setIndex(0);

        }
    }

    public void select() {

        setMyServices(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager()
                .createQuery("SELECT x FROM MyServices x ORDER BY x.serviceId").getResultList());
        System.out.println("Working");
    }
}
