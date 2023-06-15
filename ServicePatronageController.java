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

/**
 *
 * @author mis12
 */
@ManagedBean
@RequestScoped
public class ServicePatronageController implements Serializable {

    /**
     * Creates a new instance of ServicePatronageController
     */
    public ServicePatronageController() {
    }

    /*
     * properties
     */
    @ManagedProperty(value = "#{servicePatronageData}")
    private ServicePatronageData servicePatronageData;
    @ManagedProperty(value = "#{navigationController}")
    private NavigationController navigationController;
    @ManagedProperty(value = "#{customEntityManagerFactory}")
    private CustomEntityManagerFactory customEntityManagerFactory;
    @ManagedProperty(value = "#{dbConnection}")
    private DbConnection dbConnection;
    @ManagedProperty(value = "#{portalData}")
    private PortalData portalData;


    /*
     * getter setter
     */
//    public NavigationController getNavigationController() {
//        return navigationController == null ? navigationController = new NavigationController() :  navigationController;
//    }
//
//    public void setNavigationController(NavigationController navigationController) {
//        this.navigationController = navigationController;
//    }
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
        return customEntityManagerFactory;
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
        return portalData;
    }

    public void setPortalData(PortalData portalData) {
        this.portalData = portalData;
    }


    /*
     * methods
     */
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

    public Boolean newGroupValidation() {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (getServicePatronageData().getInputNewGroup() == null
                || getServicePatronageData().getInputNewGroup().equals("")) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please input a value.", ""));
            return false;
        } else {
            return true;
////       }
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
        System.out.println("Null dito");
//        getServicePatronageData().setServicesType(null);
        getServicePatronageData().setServiceDropdownDate(null);
        getServicePatronageData().setDropDown(null);

    }

    public void clearTextField() {
        getServicePatronageData().setNonMemberBirthdateInput(null);
        getServicePatronageData().setNonMemberFirstNameInput(null);
        getServicePatronageData().setNonMemberLastNameInput(null);
        getServicePatronageData().setInputNewGroup(null);
    }

    public void loadPage(String page) {

        getNavigationController().navigateTo(page);
        getServicePatronageData().setMyServices(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager()
                .createQuery("SELECT x FROM MyServices x").getResultList());
        if (getServicePatronageData().getOrgName() != null) {
            getServicePatronageData().setType(2);
        }

    }

}