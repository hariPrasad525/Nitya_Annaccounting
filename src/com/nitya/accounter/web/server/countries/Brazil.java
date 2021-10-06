package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Brazil extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Acre", "Alagoas", "Amapa",
				"Amazonas", "Bahia", "Ceara", "Distrito Federal",
				"Espirito Santo", "Goias", "Maranhao", "Mato Grosso",
				"Mato Grosso do Sul", "Minas Gerais", "Para", "Paraiba",
				"Parana", "Pernambuco", "Piaui", "Rio de Janeiro",
				"Rio Grande do Norte", "Rio Grande do Sul", "Rondonia",
				"Roraima", "Santa Catarina", "Sao Paulo", "Sergipe",
				"Tocantins" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BRL";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// TODO
		// America/Noronha Atlantic islands
		// America/Belem Amapa, E Para
		// America/Fortaleza NE Brazil (MA, PI, CE, RN, PB)
		// America/Recife Pernambuco
		// America/Araguaina Tocantins
		// America/Maceio Alagoas, Sergipe
		// America/Bahia Bahia
		// America/Sao_Paulo S & SE Brazil (GO, DF, MG, ES, RJ, SP, PR, SC, RS)
		// America/Campo_Grande Mato Grosso do Sul
		// America/Cuiaba Mato Grosso
		// America/Santarem W Para
		// America/Porto_Velho Rondonia
		// America/Boa_Vista Roraima
		// America/Manaus E Amazonas
		// America/Eirunepe W Amazonas
		// America/Rio_Branco Acre
		return "UTC-3:00 America/Sao_Paulo";
	}

}
