package ztl.Bologna.activity.Database;

import android.provider.BaseColumns;

public interface TabZtlBologna extends BaseColumns {
	
	String NOME_TABELLA = "manageZtlstreet";
	String VIA = "via";
	/*float LATITUDE = "latitude";
	float LONGITUDE = "longitude";*/
	String [] COLONNE = new String[]{VIA};
}
