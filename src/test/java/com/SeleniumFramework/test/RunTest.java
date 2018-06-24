package com.SeleniumFramework.test;

public class RunTest {

/* Set Argument 0 to Mark test cases to be executed 
 * 	Accepted Values :- 	EVPD_1,		EVPD_2,		EVPD_3
						MMIS_1,		MMIS_2,		MMIS_3
						ENB_1,		ENB_2,		ENB_3,	ENB_4
						BO_1, 		BO_2,		PB_1

*	Set Argument 1 to Generate fresh steps or not
		* By default no fresh steps will be generated 
*/
	public static void main(String[] args) {
		String flag;
		try {
			
			String phaseName="";
			
			if (args.length == 2) {
				flag = args[1];
				phaseName = args[0];
			} else if (args.length == 1){
				flag = "false";
				phaseName = args[0];
			}else{
				flag = "false";
				phaseName = "EVPD_1";
			}
			
						
			ExcelUtil.markTestCasesToBeExecutedInPhase(phaseName);
/*			OptumationApplication.run(flag);*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			//org.junit.runner.JUnitCore.main("com.SeleniumFramework.test.DriverClassTest");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
