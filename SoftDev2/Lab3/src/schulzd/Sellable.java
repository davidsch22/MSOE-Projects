package schulzd;

public interface Sellable {
    double MKE_COUNTY_TAX_RATE = 0.005;
    double WI_STATE_TAX_RATE = 0.05;

    double price();
    double tax();
}
