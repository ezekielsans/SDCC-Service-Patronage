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
    public void addNewNonMemberData() throws SQLException, ClassNotFoundException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        getDbConnection().setDbUserName(String.valueOf(getPortalData().getLiferayFacesContext().getUser().getUserId()));
        if (nonMemberValidation()) {

            try {

                getDbConnection().lportalMemOrgConnection = getDbConnection().connectToLportalMemOrg();
                getDbConnection().callableStatement = getDbConnection().lportalMemOrgConnection.prepareCall("{ ? = call add_non_member_service_patronage("
                        + "'" + getServicePatronageData().getNonMemberLastName() + "',"
                        + "'" + getServicePatronageData().getNonMemberFirstName() + "',"
                        + "'" + getServicePatronageData().getNonMemberBirthdate() + "')}");
                getDbConnection().callableStatement.registerOutParameter(1, Types.BOOLEAN);
                getDbConnection().callableStatement.execute();

            } catch (Exception e) {
                System.out.println("addnewNonMemberData - " + e);
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error on database function. ", ""));
            } finally {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Non Member Added. ", ""));
                getDbConnection().callableStatement.close();
                getDbConnection().lportalMemOrgConnection.close();

            }

            clearTextField();
        }
    }

    public Boolean nonMemberValidation() {

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();

            if (getServicePatronageData().getNonMemberBirthdate() == null
                    || getServicePatronageData().getNonMemberLastName() == null
                    || getServicePatronageData().getNonMemberFirstName() == null
                    || getServicePatronageData().getNonMemberBirthdate().equals("")
                    || getServicePatronageData().getNonMemberLastName().equals("")
                    || getServicePatronageData().getNonMemberFirstName().equals("")) {
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
        getServicePatronageData().setNonMemberLastName(null);
        getServicePatronageData().setNonMemberFirstName(null);
        getServicePatronageData().setNonMemberBirthdate(null);
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
