package cn.deepclue.datamaster.cleaner.domain.fusion;

/**
 * Created by magneto on 17-5-17.
 */
public enum  AddressCodeType {
    SINGLE_ADDRESS_CODE(0),MULTI_ADDRESS_CODE(1);
    private int addressCode;

    AddressCodeType(int addressCode) {
        this.addressCode = addressCode;
    }

    public int getAddressCode() {
        return addressCode;
    }

    public static AddressCodeType getAddressCodeType(int addressCode) {
        switch (addressCode) {
            case 0:
                return SINGLE_ADDRESS_CODE;
            case 1:
                return MULTI_ADDRESS_CODE;
            default:
                throw new IllegalStateException("Unknown address code type: " + addressCode);
        }
    }
}
