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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.MyServices;
import model.ServicePatronageNonMember;
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
    @ManagedProperty(value = "#{customEntityManagerFactory}")
    private CustomEntityManagerFactory customEntityManagerFactory;

    private List<ServicePatronageView> servicesPatronageMemberList;

    private List<MyServices> myServices;
    private List<Object[]> servicesType;
    private List<ServicePatronageNonMember> servicesPatronageNonMemberList;
    private Date birthdate;
    private Date serviceDropdownDate;
    private Date nonMemberBirthdate;
    private Date nonMemberBirthdateInput;

    private String scAcctno;
    private String lastName;
    private String firstName;
    private String addNewGroup;
    private String orgName;
    private String addedGroup;

    private String nonMemberLastName;
    private String nonMemberFirstName;
    private String nonMemberLastNameInput;
    private String nonMemberFirstNameInput;

    private String inputNewGroup;

    private Integer type;
    private Integer dropDown;
    private Integer indexNextPrev;
    private Integer index;
    /*
     * getter setter
     */

    public CustomEntityManagerFactory getCustomEntityManagerFactory() {
        return customEntityManagerFactory == null ? customEntityManagerFactory = new CustomEntityManagerFactory() : customEntityManagerFactory;
    }

    public void setCustomEntityManagerFactory(CustomEntityManagerFactory customEntityManagerFactory) {
        this.customEntityManagerFactory = customEntityManagerFactory;
    }

    public List<ServicePatronageView> getServicesPatronageMemberList() {
        return servicesPatronageMemberList == null ? servicesPatronageMemberList = new ArrayList<>() : servicesPatronageMemberList;
    }

    public void setServicesPatronageMemberList(List<ServicePatronageView> servicesPatronageMemberList) {
        this.servicesPatronageMemberList = servicesPatronageMemberList;
    }

    public List<Object[]> getServicesType() {
        return servicesType == null ? servicesType = new ArrayList<>() : servicesType;
    }

    public void setServicesType(List<Object[]> servicesType) {
        this.servicesType = servicesType;
    }

    public List<MyServices> getMyServices() {
        return myServices == null ? myServices = new ArrayList<>() : myServices;
    }

    public void setMyServices(List<MyServices> myServices) {
        this.myServices = myServices;
    }

    public List<ServicePatronageNonMember> getServicesPatronageNonMemberList() {
        return servicesPatronageNonMemberList == null ? servicesPatronageNonMemberList = new ArrayList<>() : servicesPatronageNonMemberList;
    }

    public void setServicesPatronageNonMemberList(List<ServicePatronageNonMember> servicesPatronageNonMemberList) {
        this.servicesPatronageNonMemberList = servicesPatronageNonMemberList;
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

    public Integer getDropDown() {
        return dropDown;
    }

    public void setDropDown(Integer dropDown) {
        this.dropDown = dropDown;
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

    public String getAddedGroup() {
        return addedGroup;
    }

    public void setAddedGroup(String addedGroup) {
        this.addedGroup = addedGroup;
    }

    public String getInputNewGroup() {
        return inputNewGroup;
    }

    public void setInputNewGroup(String inputNewGroup) {
        this.inputNewGroup = inputNewGroup;
    }

    public Date getNonMemberBirthdate() {
        return nonMemberBirthdate;
    }

    public void setNonMemberBirthdate(Date nonMemberBirthdate) {
        this.nonMemberBirthdate = nonMemberBirthdate;
    }

    public String getNonMemberLastName() {
        return nonMemberLastName;
    }

    public void setNonMemberLastName(String nonMemberLastName) {
        this.nonMemberLastName = nonMemberLastName;
    }

    public String getNonMemberFirstName() {
        return nonMemberFirstName;
    }

    public void setNonMemberFirstName(String nonMemberFirstName) {
        this.nonMemberFirstName = nonMemberFirstName;
    }

    public String getNonMemberLastNameInput() {
        return nonMemberLastNameInput;
    }

    public void setNonMemberLastNameInput(String nonMemberLastNameInput) {
        this.nonMemberLastNameInput = nonMemberLastNameInput;
    }

    public String getNonMemberFirstNameInput() {
        return nonMemberFirstNameInput;
    }

    public void setNonMemberFirstNameInput(String nonMemberFirstNameInput) {
        this.nonMemberFirstNameInput = nonMemberFirstNameInput;
    }

    public Date getNonMemberBirthdateInput() {
        return nonMemberBirthdateInput;
    }

    public void setNonMemberBirthdateInput(Date nonMemberBirthdateInput) {
        this.nonMemberBirthdateInput = nonMemberBirthdateInput;
    }

    public Integer getIndexNextPrev() {
        return indexNextPrev;
    }

    public void setIndexNextPrev(Integer indexNextPrev) {
        this.indexNextPrev = indexNextPrev;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

        setLastName(null);
        setFirstName(null);
        setScAcctno(null);
        setBirthdate(null);
        setType(null);
        setServicesType(null);
        setDropDown(null);
        setAddNewGroup(null);
        setNonMemberBirthdateInput(null);
        setNonMemberFirstNameInput(null);
        setNonMemberLastNameInput(null);
        setAddedGroup(null);
        setServicesPatronageNonMemberList(null);
        setNonMemberLastName(null);
        setNonMemberFirstName(null);
        setNonMemberBirthdate(null);
        setIndex(0);
    }

    public void completeMethodForServicePartronageMember() {

        String query;
        setServicesPatronageMemberList(null);

        if (getScAcctno() != null
                || getLastName() != null
                || getFirstName() != null
                || getBirthdate() != null) {

            try {

                query = "SELECT x "
                        + "FROM ServicePatronageView x "
                        + "WHERE x.scAcctno = x.scAcctno ";

                if (getScAcctno() != null) {
                    query += "AND x.scAcctno = '" + getScAcctno() + "' ";
                }
                if (getLastName() != null) {
                    query += "AND x.lastName = '" + getLastName() + "' ";
                }
                if (getFirstName() != null) {
                    query += "AND x.firstName = '" + getFirstName() + "' ";
                }
                if (getBirthdate() != null) {
                    query += "AND x.birthdate = '" + getBirthdate() + "' ";
                }

                query += "ORDER BY x.scAcctno";

                System.out.println("query: " + query);
                setServicesPatronageMemberList(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(query).getResultList());

            } catch (Exception e) {
                System.out.println("ServicePatronageData - " + e);
                setLastName(null);
                setScAcctno(null);
                setFirstName(null);
                setBirthdate(null);
            }
            setIndex(0);
            System.out.println("result " + getScAcctno());

        }

        System.out.println("out ito two");
    }

    public void completeMethodNonMember() {

        String query;

        setServicesPatronageNonMemberList(null);

        if (getNonMemberLastName() != null
                || getNonMemberFirstName() != null
                || getNonMemberBirthdate() != null) {

            System.out.println("out ito");

            try {
                query = "SELECT x "
                        + "FROM ServicePatronageNonMember x "
                        + "WHERE x.lastName = x.lastName ";

                if (getNonMemberLastName() != null) {
                    query += "AND x.lastName = '" + getNonMemberLastName() + "' ";
                }
                if (getNonMemberFirstName() != null) {
                    query += "AND x.firstName = '" + getNonMemberFirstName() + "' ";
                }
                if (getNonMemberBirthdate() != null) {
                    query += "AND x.birthdate = '" + getNonMemberBirthdate() + "' ";
                }

                query += "ORDER BY x.acctno ";

                System.out.println("magseset na");

                System.out.println("query: " + query);

                setServicesPatronageNonMemberList(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(query).getResultList());

            } catch (Exception e) {
                System.out.println("ServicePatronageData");

                System.out.println("result " + getNonMemberLastName());

            }
            System.out.println("out ito two");
        }
    }

    public void completeProfile() {
        String query;

        setServicesPatronageNonMemberList(null);

        if (getNonMemberFirstName() != null
                || getNonMemberLastName() != null) {
            try {

                query = "SELECT x "
                        + "FROM ServicePatronageNonMember x "
                        + "WHERE x.lastName = x.lastName ";
                if (getNonMemberLastName() != null) {
                    query += "AND x.lastName = '" + getNonMemberLastName().replace("'", "''") + "' ";
                }
                if (getNonMemberFirstName() != null) {

                    query += "AND x.firstName  = '" + getNonMemberFirstName().replace("'", "''") + "' ";
                }

                query += "ORDER BY x.acctno";

                setServicesPatronageNonMemberList(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(query).getResultList());

            } catch (Exception e) {
                System.out.println("controllerUpdateMemberContactandAddressInformation().profileComplete() - " + e);
            }
            setIndex(0);
        }
    }

    public void select() {

        setMyServices(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager()
                .createQuery("SELECT x FROM MyServices x ORDER BY x.serviceId").getResultList());
        System.out.println("Working");

    }

    public void clearNonMember() {
        setNonMemberBirthdate(null);
        setNonMemberFirstName(null);
        setNonMemberLastName(null);
    }

    public void previousButton(Integer indexNextPrev) {
        setIndexNextPrev(indexNextPrev - 1);

    }

    public void nextButton(Integer indexNextPrev) {
        setIndexNextPrev(indexNextPrev + 1);

    }

}
