/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import model.ServicePatronageNonMember;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author mis12
 */
@ManagedBean
@RequestScoped
public class ServicePatronageController implements Serializable {


    /*
     * properties
     */
    @ManagedProperty(value = "#{customEntityManagerFactory}")
    private CustomEntityManagerFactory customEntityManagerFactory;
    @ManagedProperty(value = "#{servicePatronageData}")
    private ServicePatronageData servicePatronageData;
    @ManagedProperty(value = "#{navigationController}")
    private NavigationController navigationController;
    @ManagedProperty(value = "#{dbConnection}")
    private DbConnection dbConnection;
    @ManagedProperty(value = "#{portalData}")
    private PortalData portalData;

    /*
     * Creates a new instance of ServicePatronageController
     */
    public ServicePatronageController() {
    }


    /*
     * getter setter
     */
    public ServicePatronageData getServicePatronageData() {
        return servicePatronageData == null ? servicePatronageData = new ServicePatronageData() : servicePatronageData;
    }

    public void setServicePatronageData(ServicePatronageData servicePatronageData) {
        this.servicePatronageData = servicePatronageData;
    }

    public NavigationController getNavigationController() {
        return navigationController == null ? navigationController = new NavigationController() : navigationController;
    }

    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    public CustomEntityManagerFactory getCustomEntityManagerFactory() {
        return customEntityManagerFactory == null ? customEntityManagerFactory = new CustomEntityManagerFactory() : customEntityManagerFactory;
    }

    public void setCustomEntityManagerFactory(CustomEntityManagerFactory customEntityManagerFactory) {
        this.customEntityManagerFactory = customEntityManagerFactory;
    }

    public DbConnection getDbConnection() {
        return dbConnection == null ? dbConnection = new DbConnection() : dbConnection;
    }

    public void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public PortalData getPortalData() {
        return portalData == null ? portalData = new PortalData() : portalData;
    }

    public void setPortalData(PortalData portalData) {
        this.portalData = portalData;
    }


    /*
     * methods
     */
    //forChanging of tabs
    public void tabChangeIndex(TabChangeEvent tabChangeEvent) {

        getServicePatronageData().beanclear();
        switch (tabChangeEvent.getTab().getTitle()) {
            case "Member":
                getServicePatronageData().setTabIndex(0);
                break;
            case "Group":
                getServicePatronageData().setTabIndex(1);
                break;
            case "Non-Member":
                getServicePatronageData().setTabIndex(2);
                break;
        }

    }

    //for member
    public List<String> findByScAcctno(String scAcctno) {
        return getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                + "SELECT DISTINCT (x.scAcctno) "
                + "FROM ServicePatronageView x "
                + "WHERE x.scAcctno LIKE :scAcctno")
                .setParameter("scAcctno", scAcctno.concat("%"))
                .getResultList();
    }

    public List<String> findByLastName(String lastName) {
        return getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                + "SELECT DISTINCT (x.lastName) "
                + "FROM ServicePatronageView x "
                + "WHERE UPPER(x.lastName) LIKE UPPER(:lastName) ")
                .setParameter("lastName", lastName.concat("%"))
                .getResultList();
    }

    public List<String> findByFirstName(String firstName) {
        return getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                + "SELECT DISTINCT (x.firstName) "
                + "FROM ServicePatronageView x "
                + "WHERE UPPER(x.firstName) LIKE UPPER(:firstName) ")
                .setParameter("firstName", firstName.concat("%")).getResultList();

    }

    public List<String> findByGroupOrg(String orgName) {
        return getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                + "SELECT DISTINCT orgsupprofile.orgName "
                + "FROM CoopFinOrgSupProfile orgsupprofile "
                + "WHERE UPPER(orgsupprofile.orgName) LIKE UPPER(:orgName) ")
                .setParameter("orgName", orgName.concat("%")).getResultList();

    }

    //for group
    public List<String> findAddedGroup(String addedGroup) {
        return getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                + "SELECT DISTINCT (x.groupName) "
                + "FROM ServicePatronageGroup x "
                + "WHERE UPPER (x.groupName) LIKE UPPER(:groupName) ")
                .setParameter("groupName", addedGroup.concat("%")).getResultList();

    }

//    for non-member
    public List<String> findNonMemberByLastName(String nonMemberLastName) {

        return getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                + "SELECT DISTINCT (x.lastName) "
                + "FROM ServicePatronageNonMember x "
                + "WHERE UPPER (x.lastName) LIKE UPPER(:lastName) ")
                .setParameter("lastName", nonMemberLastName.concat("%"))
                .getResultList();

    }

    public List<String> findNonMemberByFirstName(String nonMemberFirstName) {

        return getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                + "SELECT DISTINCT (x.firstName) "
                + "FROM ServicePatronageNonMember x "
                + "WHERE UPPER (x.firstName) LIKE UPPER(:firstName) ")
                .setParameter("firstName", nonMemberFirstName.concat("%"))
                .getResultList();

    }

//add function for group 
    public void addNewGroupDataToTable() throws SQLException, ClassNotFoundException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        getDbConnection().setDbUserName(String.valueOf(getPortalData().getLiferayFacesContext().getUser().getUserId()));

        if (newGroupValidation()) {

            try {
                getDbConnection().lportalMemOrgConnection = getDbConnection().connectToLportalMemOrg();
                getDbConnection().callableStatement = getDbConnection().lportalMemOrgConnection.prepareCall("{ ? = call add_group_service_patronage ("
                        + "'" + getServicePatronageData().getInputNewGroup() + "')}");
                getDbConnection().callableStatement.registerOutParameter(1, Types.BOOLEAN);
                getDbConnection().callableStatement.execute();

            } catch (Exception e) {
                System.out.println("addNewGroupDataToTable" + e);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error on database function. ", ""));
            } finally {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Group Added Successfully. ", ""));
                getDbConnection().callableStatement.close();
                getDbConnection().lportalMemOrgConnection.close();

            }
            clearTextField();
        }
    }

//for adding new non member
    public void addNewNonMemberData() throws SQLException, ClassNotFoundException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        getDbConnection().setDbUserName(String.valueOf(getPortalData().getLiferayFacesContext().getUser().getUserId()));
        if (nonMemberValidation()) {

            try {

                getDbConnection().lportalMemOrgConnection = getDbConnection().connectToLportalMemOrg();
                getDbConnection().callableStatement = getDbConnection().lportalMemOrgConnection.prepareCall("{ ? = call add_non_member_service_patronage("
                        + "'" + getServicePatronageData().getNonMemberLastNameInput() + "',"
                        + "'" + getServicePatronageData().getNonMemberFirstNameInput() + "',"
                        + "'" + getServicePatronageData().getNonMemberBirthdateInput() + "')}");
                getDbConnection().callableStatement.registerOutParameter(1, Types.BOOLEAN);
                getDbConnection().callableStatement.execute();

            } catch (Exception e) {
                System.out.println("addnewNonMemberData - " + e);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error on database function. ", ""));
            } finally {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Non Member Added Successfully. ", ""));
                getDbConnection().callableStatement.close();
                getDbConnection().lportalMemOrgConnection.close();

            }

            clearTextField();
        }
    }

    //validation for load page
    public Boolean newGroupValidation() {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (getServicePatronageData().getInputNewGroup() == null
                || getServicePatronageData().getInputNewGroup().equals("")) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please input a value.", ""));
            return false;
        } else {
            return true;

        }

    }

    //    public void addNewGroup() throws SQLException ,ClassNotFoundException
    public Boolean nonMemberValidation() {

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();

            if (getServicePatronageData().getNonMemberBirthdateInput() == null
                    || getServicePatronageData().getNonMemberFirstNameInput() == null
                    || getServicePatronageData().getNonMemberLastNameInput() == null
                    || getServicePatronageData().getNonMemberFirstNameInput().equals("")
                    || getServicePatronageData().getNonMemberLastNameInput().equals("")) {

                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please input a value in the specific fields.", ""));
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;

        }

    }

    public void addServiceDateToTable() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        //try {
        if (getServicePatronageData().getDropDown() == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select Service.", ""));
        } else {
            System.out.println("dropdown: " + getServicePatronageData().getDropDown());
            System.out.println("date" + getServicePatronageData().getServiceDropdownDate());
            getServicePatronageData().getServicesType().add(new Object[]{
                getServicePatronageData().getDropDown(),
                getServicePatronageData().getServiceDropdownDate()

            });
        }

//        getServicePatronageData().setServicesType(null);
        getServicePatronageData().setServiceDropdownDate(null);
        getServicePatronageData().setDropDown(null);
        getServicePatronageData().setMyOffices(null);

    }

//clear function for non member
    public void clearTextField() {
        getServicePatronageData().setNonMemberBirthdateInput(null);
        getServicePatronageData().setNonMemberFirstNameInput(null);
        getServicePatronageData().setNonMemberLastNameInput(null);
        getServicePatronageData().setInputNewGroup(null);
    }

    public void loadPage(String page) {
        System.out.println("grp: " + getServicePatronageData().getAddedGroup());
        System.out.println("tabIndex " + getServicePatronageData().getTabIndex());

        getNavigationController().navigateTo(page);
        getServicePatronageData().setMyServices(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager()
                .createQuery("SELECT x FROM MyServices x").getResultList());

        getServicePatronageData().setMyOffices(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager()
                .createQuery("SELECT x FROM MyOffices x").getResultList());

        if (getServicePatronageData().getOrgName() != null) {
            getServicePatronageData().setType(0);
        }

        if (getServicePatronageData().getTabIndex() == 0) {
            System.out.println("tabindex1 " + getServicePatronageData().getTabIndex());

            getServicePatronageData().setLastName(getServicePatronageData().getServicesPatronageMemberList().get(0).getLastName());

            getServicePatronageData().setFirstName(getServicePatronageData().getServicesPatronageMemberList().get(0).getFirstName());
            getServicePatronageData().setFullName(getServicePatronageData().getLastName().concat(" ").concat(getServicePatronageData().getFirstName()));
        } else if (getServicePatronageData().getTabIndex() == 1) {
            getServicePatronageData().getAddedGroup();
        } else if (getServicePatronageData().getTabIndex() == 2) {

            getServicePatronageData().setNonMemberLastName(getServicePatronageData().getServicesPatronageNonMemberList().get(0).getLastName());
            getServicePatronageData().setNonMemberFirstName(getServicePatronageData().getServicesPatronageNonMemberList().get(0).getFirstName());
            getServicePatronageData().setFullNameNonMember(getServicePatronageData().getNonMemberLastName().concat(" ").concat(getServicePatronageData().getNonMemberFirstName()));
        }
    }

}

//        try {
//            System.out.println("grp1: " + getServicePatronageData().getAddedGroup());
////            System.out.println("showName1: " + getServicePatronageData().getShowName());
////            getServicePatronageData().setShowName(false);
//            getServicePatronageData().setAddedGroup(getServicePatronageData().getServicePatronageGroupList().get(0).getGroupName());
//        } catch (Exception e) {
//            System.out.println("qwe" + e);
//        }
//
//        try {
////            getServicePatronageData().setShowName(true);
//        getServicePatronageData().setLastName(getServicePatronageData().getServicesPatronageMemberList().get(0).getLastName());
//        getServicePatronageData().setFirstName(getServicePatronageData().getServicesPatronageMemberList().get(0).getFirstName());
//        } catch (Exception e) {
//            System.out.println("asd" + e);
//        }
//
//        try {
////            getServicePatronageData().setShowName(true);
//            getServicePatronageData().setNonMemberLastName(getServicePatronageData().getServicesPatronageNonMemberList().get(0).getLastName());
//            getServicePatronageData().setNonMemberFirstName(getServicePatronageData().getServicesPatronageNonMemberList().get(0).getFirstName());
//
//        } catch (Exception e) {
//            System.out.println("zxc" + e);
//        }
//
//        System.out.println("showName2: " + getServicePatronageData());
