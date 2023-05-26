/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.util.List;

/**
 *
 * @author mis12
 */
@ManagedBean
@RequestScoped
public class ServicePatronageAutoCompleteForm implements Serializable {

    /**
     * Creates a new instance of ServicePatronageAutoCompleteForm
     */
    public ServicePatronageAutoCompleteForm() {
    }

    /*
     * properties
     */
    @ManagedProperty(value = "#{customEntityManagerFactory}")
    private CustomEntityManagerFactory customEntityManagerFactory;
    @ManagedProperty(value = "#{servicePatronageData}")
    private ServicePatronageData servicePatronageData;
    @ManagedProperty(value = "#{servicePatronageAutoCompleteForm}")
    private ServicePatronageAutoCompleteForm servicePatronageAutoCompleteForm;


    /*
     * getter setter
     */
    public CustomEntityManagerFactory getCustomEntityManagerFactory() {
        return customEntityManagerFactory == null ? customEntityManagerFactory = new CustomEntityManagerFactory() : customEntityManagerFactory;
    }

    public void setCustomEntityManagerFactory(CustomEntityManagerFactory customEntityManagerFactory) {
        this.customEntityManagerFactory = customEntityManagerFactory;
    }

    public ServicePatronageData getServicePatronageData() {
        return servicePatronageData == null ? servicePatronageData = new ServicePatronageData() : servicePatronageData;
    }

    public void setServicePatronageData(ServicePatronageData servicePatronageData) {
        this.servicePatronageData = servicePatronageData;
    }

    public ServicePatronageAutoCompleteForm getServicePatronageAutoCompleteForm() {
        return servicePatronageAutoCompleteForm;
    }

    public void setServicePatronageAutoCompleteForm(ServicePatronageAutoCompleteForm servicePatronageAutoCompleteForm) {
        this.servicePatronageAutoCompleteForm = servicePatronageAutoCompleteForm;
    }

    /*
     * methods
     */
//    public List<String> autoCompleteServicesPatronageFields(String scAcctno) {
//        return getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
//                + "SELECT a.scAcctno "
//                + "FROM ServicePatronageView a ")
//                .setParameter("scAcctno", scAcctno.concat("%"))
//                .getResultList();
//    }
    public List<String> findByScAcctno(String scAcctno) {
        return getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                + "SELECT DISTINCT (x.scAcctno) "
                + "FROM ServicePatronageView x "
                + "WHERE UPPER(x.scAcctno) LIKE UPPER(:scAcctno) "
                + "OR SUBSTRING(x.scAcctno, 3, 7) LIKE :scAcctno")
                .setParameter("scAcctno", scAcctno.concat("%"))
                .getResultList();
    }

}
