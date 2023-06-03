/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

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


    /*
     * methods
     */
    public void loadPage(String page) {

        getNavigationController().navigateTo(page);

        getServicePatronageData().setMyServices(getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager()
                .createQuery("SELECT x FROM MyServices x").getResultList());

        if (getServicePatronageData().getOrgName() != null) {
            getServicePatronageData().setType(2);
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
}
