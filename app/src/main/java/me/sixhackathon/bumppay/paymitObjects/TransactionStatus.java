package me.sixhackathon.bumppay.paymitObjects;

public enum TransactionStatus {
    OPEN (1, "Open"),
    DONE(2, "Done"),
    DONENOUSER(3, "Done but receiver doesn't have a user"),
    PENDING(4, "Pending"),
    PENDINGNOUSER(5, "Pending but sender doesn't have a user"),
    ACCEPTED(6, "Accepted"),
    REJECTED(7,"Rejected");

    public static TransactionStatus findById(int id){
        for (TransactionStatus transactionStatus : TransactionStatus.values()) {
            if(transactionStatus.getStatusId() == id){
                return transactionStatus;
            }
        }
        return TransactionStatus.REJECTED;
    }

    public int getStatusId() {
        return statusId;
    }

    private final int statusId;

    private final String statusString;

    TransactionStatus(int anStature, String anStatureString) { statusId = anStature; statusString = anStatureString; }

    public int getValue() { return statusId; }

    @Override
    public String toString() { return statusString; }
}
