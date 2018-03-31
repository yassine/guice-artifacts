package com.github.yassine.artifacts.guice.utils;

import com.google.auto.service.AutoService;
import com.google.inject.Inject;
import lombok.Getter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Fixtures {

  public interface Contract {

  }

  public static class ContractImplementation1 implements Contract{

  }


  public static class ContractImplementation2 implements Contract{

  }

  @ScanIgnore
  public static class ContractImplementation3 implements Contract{

  }

  @IgnoreMe
  public static class ContractImplementation4 implements Contract{

  }

  @Retention(RetentionPolicy.RUNTIME)
  public @interface IgnoreMe{

  }


  public interface SPIContract {

  }

  @AutoService(SPIContract.class)
  public static class SPIContractImplementation1 implements SPIContract{

  }

  @AutoService(SPIContract.class)
  public static class SPIContractImplementation2 implements SPIContract {
    @Inject @Getter
    private SPIContractImplementation1 spiContractImplementation1;
  }




}
