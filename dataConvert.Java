/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.TemporalType;
import model.CoopFinClassTd;
import model.CoopFinTdProdDtl;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author misteam
 */
@ManagedBean
@SessionScoped
public class DataConvert implements Serializable {

    /**
     * Creates a new instance of DataConvert
     */
    public DataConvert() {
    }

    /*
     * properties
     */
    @ManagedProperty(value = "#{customEntityManagerFactory}")
    private CustomEntityManagerFactory customEntityManagerFactory;

    /*
     * getter setter
     */
    public CustomEntityManagerFactory getCustomEntityManagerFactory() {
        return customEntityManagerFactory == null ? customEntityManagerFactory = new CustomEntityManagerFactory() : customEntityManagerFactory;
    }

    public void setCustomEntityManagerFactory(CustomEntityManagerFactory customEntityManagerFactory) {
        this.customEntityManagerFactory = customEntityManagerFactory;
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

    }

    public String acctStatusConvert(Character acctStatus) {
        switch (acctStatus) {
            case 'A':
                return "ACTIVE";
            case 'C':
                return "CLOSED";
            case 'D':
                return "DORMANT";
            case 'U':
                return "UNUPDATED";
            case 'I':
                return "INACTIVE";
            case 'O':
                return "O";
            default:
                return "-";
        }
    }

    public String genderConvert(Character gender) {
        switch (gender) {
            case 'M':
                return "MALE";
            case 'F':
                return "FEMALE";
            default:
                return "-";
        }
    }

    public String civilStatusConvert(Character civilStatus) {
        switch (civilStatus) {
            case 'S':
                return "SINGLE";
            case 'M':
                return "MARRIED";
            case 'P':
                return "SEPARATED";
            case 'W':
                return "WIDOW(ER)";
            case 'N':
                return "NO DATA";
            default:
                return "-";
        }
    }

    public String holderRoleConvert(Character holderRole) {
        switch (holderRole) {
            case 'P':
                return "PRINCIPAL";
            case 'S':
                return "SECONDARY";
            case 'G':
                return "SIGNER";
            case 'C':
                return "CO-SIGNER";
            case 'I':
                return "AUTHORIZED TO INQUIRE";
            default:
                return "-";
        }
    }

    public String holderTypeConvert(Character holderType) {
        switch (holderType) {
            case 'R':
                return "REGULAR";
//                return "Single, Regular Member";
            case 'A':
                return "ASSOCIATE";
//                return "Single, Associate Member";
            case 'J':
                return "JOINT";
//                return "Joint, Member with Special Depositor";
            case 'G':
                return "ORGANIZATION";
//                return "Group or Organization";
            default:
                return "-";
        }
    }

    public String acctRestrictionConvert(String acctRestriction) {
        switch (acctRestriction) {
            case "N":
                return "NONE";
            case "A":
                return "AND";
            case "O":
                return "OR";
            case "I":
                return "ITF(minor)";
            default:
                return acctRestriction;
        }
    }

    public String tdProdCodeConvert(String tdProdCode) {
        try {
            return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT tpf.tdProdName "
                    + "FROM CoopFinTdProdFile tpf "
                    + "WHERE tpf.tdProdCode = :tdProdCode").setParameter("tdProdCode", tdProdCode).getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public String termLengthConvert(String acctno) {
        try {
            long term;
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();

            CoopFinClassTd td = (CoopFinClassTd) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cls FROM CoopFinClassTd cls "
                    + "WHERE cls.acctno.acctno = :acctno")
                    .setParameter("acctno", acctno)
                    .getResultList().get(0);

            cal1.setTime(td.getTermStart());
            cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE), 00, 00, 00);
            cal2.setTime(td.getTermEnd());
            cal2.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DATE), 00, 00, 00);
            term = Math.round((cal1.getTime().getTime() - cal2.getTime().getTime()) / (24 * 60 * 60 * 1000.00));
            term = Math.abs(term);

            System.out.print("Number of days from term start to term end: " + term);
            if (td.getTdProdCode().getMinTerm() == 1) {
                term = 31;
            } else {
                term = (term / 365) * 365;
            }

            CoopFinTdProdDtl detail = (CoopFinTdProdDtl) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT pd "
                    + "FROM CoopFinTdProdDtl pd "
                    + "WHERE pd.tdProdCode = :tdProdCode "
                    + "AND pd.effectiveFrom IN (SELECT MAX(t2.effectiveFrom) "
                    + "FROM CoopFinTdProdDtl t2 "
                    + "WHERE t2.tdProdCode = :tdProdCode "
                    + "AND t2.effectiveFrom <= :termStart) "
                    + "AND pd.amtBracket <= :depositAmt "
                    + "AND pd.termLength = :term "
                    + "ORDER BY pd.amtBracket DESC")
                    .setParameter("tdProdCode", td.getTdProdCode().getTdProdCode().charAt(0))
                    .setParameter("termStart", td.getTermStart(), TemporalType.DATE)
                    .setParameter("depositAmt", td.getDepositAmount())
                    .setParameter("term", term)
                    .getResultList().get(0);
            return detail.getTermLength().toString();
        } catch (Exception e) {
            return "-";
        }
    }

    public String accountCodeConvert(String accountCode) {
        try {
            return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT coa.acctTitle "
                    + "FROM CoopFinChartOfAccounts coa "
                    + "WHERE coa.accountCode = :accountCode").setParameter("accountCode", accountCode).getResultList().get(0);
        } catch (Exception e) {
            return accountCode;
        }
    }

    public String numericConvert(Object numeric) {
        return numeric == null ? "" : new DecimalFormat("###,##0.00").format(numeric);
    }

    public String monthConvert(Integer month) {
        switch (month) {
            case 1:
                return "JANUARY";
            case 2:
                return "FEBRUARY";
            case 3:
                return "MARCH";
            case 4:
                return "APRIL";
            case 5:
                return "MAY";
            case 6:
                return "JUNE";
            case 7:
                return "JULY";
            case 8:
                return "AUGUST";
            case 9:
                return "SEPTEMBER";
            case 10:
                return "OCTOBER";
            case 11:
                return "NOVEMBER";
            case 12:
                return "DECEMBER";
            default:
                return "-";
        }
    }

    public String codeConvert(Integer code) {
        try {
            return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT a.aprvrName "
                    + "FROM CoopFinApprover a "
                    + "WHERE a.aprvrCode = :code").setParameter("code", code).getResultList().get(0);
        } catch (Exception e) {
            return "";
        }
    }

    public String damayanStatusConvert(Character damayanStatus) {
        switch (damayanStatus) {
            case 'A':
                return "ACTIVE";
            case 'C':
                return "CLOSED";
            case 'T':
                return "TERMINATED";
            case 'D':
                return "DECEASED";
            case 'I':
                return "I";
            default:
                return "-";
        }
    }

    public String quarterConvert(Integer quarter) {
        switch (quarter) {
            case 1:
                return "1ST QUARTER";
            case 2:
                return "2ND QUARTER";
            case 3:
                return "3RD QUARTER";
            case 4:
                return "4TH QUARTER";
            default:
                return "-";
        }
    }

    public String isPrimaryHolder(String primaryHolder, String holderProfileId) {
        return primaryHolder.isEmpty() ? "" : primaryHolder.equals(holderProfileId) ? "YES" : "NO";
    }

    public String holderProfileName(String holderProfileId) {
        try {
            return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT CONCAT(UPPER(hp.lastName),', ',UPPER(hp.firstName),COALESCE(CONCAT(' ',UPPER(hp.middleName)), '')) "
                    + "FROM CoopFinHolderProfile hp "
                    + "WHERE hp.holderProfileId = :holderProfileId").setParameter("holderProfileId", holderProfileId).getSingleResult();
        } catch (Exception e) {
            return "-";
        }
    }

    public String installment(Boolean installment) {
        return installment ? "YES" : "NO";
    }

    public String autoRollover(Boolean autoRollover) {
        try {
            return autoRollover ? "YES" : "NO";
        } catch (Exception e) {
            return "-";
        }
    }

    public String tdProdName(Boolean tdProdName) {
        return tdProdName ? "TRUE" : "FALSE";
    }

    public String paymentType(Boolean paymentType) {
        return paymentType ? "YES" : "NO";
    }

    public String findAcctName(String particulars, String acctTitle, Boolean isBank) {
//        if (acctTitle.startsWith("111103")) {
//            return "";
//        } else {
        String acctnoPrefix = "", acctno = "", acctName = "";

        try {
            acctnoPrefix = (particulars.substring(particulars.length() - 9, particulars.length())).substring(0, 2);
        } catch (Exception e) {
            System.out.print("dataConvert().findAcctName()-0 " + e);
        }

        try {
            acctno = particulars.substring(particulars.length() - 9, particulars.length());
        } catch (Exception e) {
            System.out.print("dataConvert().findAcctName()-1 " + e);
        }

        try {
            acctTitle = accountCodeConvert(acctTitle);
            acctName = particulars.toUpperCase().replace(acctTitle.toUpperCase() + " - ", "").replace(acctTitle.toUpperCase(), "");
        } catch (Exception e) {
            System.out.print("dataConvert().findAcctName()-2 " + e);
        }

        try {
            switch (acctnoPrefix) {
                case "SC":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassSc x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "SD":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassSd x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "PC":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassSd x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "SS":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassSd x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "AF":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassSd x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "TD":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassTd x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "DM":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassDm x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "RI":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassRi x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "OR":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassOt x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "CA":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassCa x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "OP":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctName "
                            + "FROM CoopFinClassOp x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "BK":
                    if (isBank) {
                        return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                                + "SELECT x.bankName "
                                + "FROM CoopFinBankProfile x "
                                + "WHERE x.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                    }
//                    } else {
//                        return particulars.replace(" - " + acctno, "");
//                    }
                default:
                    if (!isBank) {
//                        return particulars.replace(" - " + acctno, "");
                        acctName = particulars.toUpperCase().replace(acctTitle.toUpperCase() + " - ", "").replace(acctTitle.toUpperCase(), "");
//                        return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
//                                + "SELECT CONCAT(UPPER(lastName),', ',UPPER(firstName),COALESCE(CONCAT(' ',UPPER(middleName)), '')) "
//                                + "FROM CoopFinHolderProfile "
//                                + "WHERE CONCAT(UPPER(lastName),', ',UPPER(firstName),COALESCE(CONCAT(' ',UPPER(middleName)), '')) = UPPER(:acctName)")
//                                .setParameter(":acctName", acctName).getResultList().get(0) == null ? "" : acctName;
                    }
                    return acctName;
            }
        } catch (Exception e) {
            System.out.print("dataConvert().findAcctName()-3 " + e);
        }
        return "";
//        }
    }

    public String findAcctno(String particulars, Boolean isBank) {
        String acctnoPrefix = "", acctno = "";

        try {
            acctnoPrefix = (particulars.substring(particulars.length() - 9, particulars.length())).substring(0, 2);
        } catch (Exception e) {
            System.out.print("dataConvert().findAcctno()-0 " + e);
        }

        try {
            acctno = particulars.substring(particulars.length() - 9, particulars.length());
        } catch (Exception e) {
            System.out.print("dataConvert().findAcctno()-1 " + e);
        }

        try {
            switch (acctnoPrefix) {
                case "SC":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassSc x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "SD":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassSd x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "PC":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassSd x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "SS":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassSd x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "AF":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassSd x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "TD":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassTd x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "DM":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassDm x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "RI":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassRi x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "OR":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassOt x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "CA":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassCa x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "OP":
                    return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                            + "SELECT x.acctno.acctno "
                            + "FROM CoopFinClassOp x "
                            + "WHERE x.acctno.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                case "BK":
                    if (isBank) {
                        return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                                + "SELECT x.acctno "
                                + "FROM CoopFinBankProfile x "
                                + "WHERE x.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
                    }
//                    } else {
//                        return particulars.replace(" - " + acctno, "");
//                    }
                default:
                    return "";
            }
        } catch (Exception e) {
            System.out.print("dataConvert().findAcctno()-2 " + e);
        }

        return "";
    }
//    hard-coded account_code

    public Boolean isBank(String accountCode) {
        return accountCode.startsWith("111106") || accountCode.startsWith("111109");
    }

    public String bankName(String acctno) {
        try {
            return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT bp.bankName "
                    + "FROM CoopFinBankProfile bp "
                    + "WHERE bp.acctno = :acctno").setParameter("acctno", acctno).getResultList().get(0);
        } catch (Exception e) {
            return "";
        }
    }

    public String entryTypeConvert(String entryType) {
        switch (entryType) {
            case "D":
                return "Debit";
            case "C":
                return "Credit";
            default:
                return "-";
        }
    }

    public Integer findCtdNoByAcctno(String acctno) throws IndexOutOfBoundsException {
        return (Integer) (getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager()
                .createQuery("SELECT cls.ctdNo "
                        + "FROM CoopFinClassTd cls "
                        + "WHERE cls.acctno.acctno = :acctno")
                .setParameter("acctno", acctno)
                .getResultList().get(0));
    }

    public String findLocation(Integer location) {
        return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                + "SELECT ol.name "
                + "FROM OrganizationLocation ol "
                + "WHERE ol.organizationLocationId = :location")
                .setParameter("location", location + 1)
                .getSingleResult();
    }

    public String convertAmount(BigDecimal amount) {
        return new DecimalFormat("###,##0.00").format(amount);
    }

    public String convertTdProdCode(String tdProdCode) {
        return (String) getCustomEntityManagerFactory()
                .getFinancialDbEntityManagerFactory()
                .createEntityManager()
                .createQuery("SELECT tpf.tdProdName "
                        + "FROM CoopFinTdProdFile tpf "
                        + "WHERE tpf.tdProdCode = :tdProdCode")
                .setParameter("tdProdCode", tdProdCode)
                .getResultList()
                .get(0);
    }

    public String convertSdToScAcctno(String acctno) {
        return acctno.replace("SD", "SC");
    }

    public String residenceTypeConv(String residenceType) {
        switch (residenceType) {
            case "O":
                return "OWNED";
            case "R":
                return "RENTED";
            case "L":
                return "LIVING WITH RELATIVES";
            case "M":
                return "MORTGAGE";
            default:
                return " ";
        }
    }

    public String contactTypeConv(char contactType) {
        switch (contactType) {
            case 'E':
                return "EMAIL";
            case 'M':
                return "MOBILE";
            case 'T':
                return "TELEPHONE";
            case 'F':
                return "FAX";
            case 'L':
                return "LANDLINE";
            case 'Z':
                return "FACEBOOK";
            default:
                return " ";
        }
    }

    public String memberStatusConv(Integer memberStatus) {
        switch (memberStatus) {
            case 4:
                return "WITHDRAWN";
            case 6:
                return "EXPELLED";
            case 7:
                return "DROPPED";
            case 10:
                return "PROBATIONARY";
            case 11:
                return "NO DATA";
            case 1:
                return "GOOD STANDING";
            case 2:
                return "NOT IN GOOD STANDING";
            default:
                return " ";
        }
    }

    public String memberLoanStatusConv(String loanStatus) {
        switch (loanStatus) {
            case "R":
                return "Reminder";
            case "N":
                return "Notice";
            case "F":
                return "Follow up";
            case "L":
                return "Legal";
            case "T":
                return "For Drastic Action";
            case "D":
                return "Under Court";

//            case "D":
//                return "First Demand";
//            //GREATER THAN 90 DAYS MISPAYMENT
//            case "L":
//                return "Due Demandable";
//            //REACHED 20% PRINCIPAL AMT_GRNTD
//            case "N":
//                return "Notice";
//            //90 DAYS PAG MISPAYMENT
//            case "R":
//                return "Regular";
//            case "T":
//                return "Past Due";
            default:
                return " ";
        }
    }

    public String memberLoanCodeConv(String loanCode) {
        return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createNativeQuery(""
                + "SELECT lt.description "
                + "FROM loan_type lt "
                + "WHERE lt.loan_code = '" + loanCode + "'")
                .getResultList().get(0);
    }

    public String ouCodeConv(String ouCode) {
        try {
            return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.ouShortName "
                    + "FROM CoopOrgUnit cw "
                    + "WHERE cw.ouCode = :ouCode")
                    .setParameter("ouCode", ouCode)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public String ouCodeConvUpdate(String ouCode) {
        try {
            return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.ouShortName "
                    + "FROM CoopOrgUnit cw "
                    + "WHERE cw.ouCode = :ouCode")
                    .setParameter("ouCode", ouCode)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public Integer ouShortName(String ouShortName) {
        return (Integer) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                + "SELECT cw.ouCode "
                + "FROM CoopOrgUnit cw "
                + "WHERE cw.ouShortName = :ouShortName")
                .setParameter("ouShortName", ouShortName)
                .getResultList().get(0);
    }

    public String luContactTypeConv(String luContactTypeId) {
        try {
            return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.luContactType "
                    + "FROM CoopLuContactType cw "
                    + "WHERE cw.luContactTypeId = :luContactTypeId")
                    .setParameter("luContactTypeId", luContactTypeId)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public String luContactTypeId(String luContactType) {
        try {
            return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.luContactTypeId "
                    + "FROM CoopLuContactType cw "
                    + "WHERE cw.luContactType = :luContactType")
                    .setParameter("luContactType", luContactType)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public String luCountry(String country) {
        switch (country) {
            case "A":
                return "ABROAD";
            case "P":
                return "PHILIPPINES";

            default:
                return " ";
        }
    }

    public String luAddressRegionId(Integer regionId) {
        try {
            return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.region FROM CoopLuRegion cw WHERE cw.regionId = :regionId")
                    .setParameter("regionId", regionId)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public String luAddressCityId(Integer cityId) {
        try {
            return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.city FROM CoopLuCity cw WHERE cw.cityId = :cityId")
                    .setParameter("cityId", cityId)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public String luAddressBarangayId(Integer barangayId) {
        try {
            return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.barangay FROM CoopLuBarangay cw WHERE cw.barangayId = :barangayId")
                    .setParameter("barangayId", barangayId)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public String luAddressProvinceId(Integer provinceId) {
        try {
            return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.province FROM CoopLuProvince cw WHERE cw.provinceId = :provinceId")
                    .setParameter("provinceId", provinceId)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public String luAddressAddressType(String luAddressTypeId) {
        try {
            return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.luAddressType FROM CoopLuAddressType cw WHERE cw.luAddressTypeId = :luAddressTypeId")
                    .setParameter("luAddressTypeId", luAddressTypeId)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public String luAddressResidenceType(String luResidenceTypeId) {
        try {
            return (String) getCustomEntityManagerFactory().getLportalMemOrgEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.luResidenceType FROM CoopLuResidenceType cw WHERE cw.luResidenceTypeId = :luResidenceTypeId")
                    .setParameter("luResidenceTypeId", luResidenceTypeId)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }
    }

    public String loanLocation(Integer locationId) {
        switch (locationId) {
            case 0:
                return "Main";
            case 1:
                return "Satelitte - DASA";
            case 2:
                return "Satelitte - LPC";
            case 3:
                return "Salitte - Tanza";
            case 999:
                return "ALL";
            default:
                return " ";
        }
    }

    public String tableName(Integer tableId) {

        try {
            return (String) getCustomEntityManagerFactory().getFinancialDbEntityManagerFactory().createEntityManager().createQuery(""
                    + "SELECT cw.bookName FROM BookLu cw WHERE cw.bookId = :bookId")
                    .setParameter("bookId", tableId)
                    .getResultList().get(0);
        } catch (Exception e) {
            return "-";
        }

    }

    public String typeConvert(Integer type) {
        switch (type) {
            case 0:
                return "Member";
            case 1:
                return "Group";
            case 2:
                return "Non-Member";

        }
        return "";
    }

    public String serviceTypeConvert(Integer serviceId) {
        return (String) getCustomEntityManagerFactory()
                .getLportalMemOrgEntityManagerFactory()
                .createEntityManager()
                .createQuery("SELECT x.serviceName "
                        + "FROM MyServices x "
                        + "WHERE x.serviceId = :serviceId ")
                .setParameter("serviceId", serviceId)
                .getResultList()
                .get(0);
    }

}
