package org.swe.model;

public final class VerificationSessionResult {
     private final String userIdentity;
     private final VerifySessionStatus status;
     private final int quantity;

     public VerificationSessionResult(String guestIdentity, VerifySessionStatus status, int quantity) {
          this.userIdentity = guestIdentity;
          this.status = status;
          this.quantity = quantity;
     }

     public String getUserIdentity() {
          return userIdentity;
     }
     
     public VerifySessionStatus getStatus() {
          return status;
     }

     public int getQuantity() {
          return quantity;
     }
}
