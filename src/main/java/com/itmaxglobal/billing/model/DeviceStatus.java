package com.itmaxglobal.billing.model;

import java.util.stream.Stream;

public enum DeviceStatus {

   Green(0), Red(1), Orange(2), Stolen(3), In_Progress(4), Unknown(99);

   private int code;

   DeviceStatus(int code)
   {
      this.code = code;
   }

   public int getCode()
   {
      return code;
   }

   public static DeviceStatus valueOf(int code)
   {
      return Stream.of(DeviceStatus.values()).filter(deviceStatus -> deviceStatus.getCode() == code).findFirst().orElseThrow(IllegalArgumentException::new);
   }
}