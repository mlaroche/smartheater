package com.mlaroche.smartheater.domain;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.structure.model.Entity;
import io.vertigo.datastore.impl.entitystore.EnumStoreVAccessor;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datastore.impl.entitystore.StoreVAccessor;
import io.vertigo.datamodel.structure.stereotype.Field;
import io.vertigo.datamodel.structure.util.DtObjectUtil;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class Heater implements Entity {
	private static final long serialVersionUID = 1L;

	private Long heaId;
	private String name;
	private String dnsName;
	private Boolean active;
	private Boolean auto;
	private java.time.Instant autoSwitch;

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "AHeaWca",
			fkFieldName = "wcaId",
			primaryDtDefinitionName = "DtWeeklyCalendar",
			primaryIsNavigable = true,
			primaryRole = "WeeklyCalendar",
			primaryLabel = "Calendrier",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtHeater",
			foreignIsNavigable = false,
			foreignRole = "Heater",
			foreignLabel = "Radiateur",
			foreignMultiplicity = "0..*")
	private final StoreVAccessor<com.mlaroche.smartheater.domain.WeeklyCalendar> wcaIdAccessor = new StoreVAccessor<>(com.mlaroche.smartheater.domain.WeeklyCalendar.class, "WeeklyCalendar");

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "AHeaPro",
			fkFieldName = "proCd",
			primaryDtDefinitionName = "DtProtocol",
			primaryIsNavigable = true,
			primaryRole = "Protocol",
			primaryLabel = "Protocol",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtHeater",
			foreignIsNavigable = false,
			foreignRole = "Heater",
			foreignLabel = "Radiateur",
			foreignMultiplicity = "0..*")
	private final EnumStoreVAccessor<com.mlaroche.smartheater.domain.Protocol, com.mlaroche.smartheater.domain.ProtocolEnum> proCdAccessor = new EnumStoreVAccessor<>(com.mlaroche.smartheater.domain.Protocol.class, "Protocol", com.mlaroche.smartheater.domain.ProtocolEnum.class);

	@io.vertigo.datamodel.structure.stereotype.Association(
			name = "AHeaMod",
			fkFieldName = "modCd",
			primaryDtDefinitionName = "DtHeaterMode",
			primaryIsNavigable = true,
			primaryRole = "Mode",
			primaryLabel = "Mode",
			primaryMultiplicity = "1..1",
			foreignDtDefinitionName = "DtHeater",
			foreignIsNavigable = false,
			foreignRole = "Heater",
			foreignLabel = "Radiateur",
			foreignMultiplicity = "0..*")
	private final EnumStoreVAccessor<com.mlaroche.smartheater.domain.HeaterMode, com.mlaroche.smartheater.domain.HeaterModeEnum> modCdAccessor = new EnumStoreVAccessor<>(com.mlaroche.smartheater.domain.HeaterMode.class, "Mode", com.mlaroche.smartheater.domain.HeaterModeEnum.class);

	/** {@inheritDoc} */
	@Override
	public UID<Heater> getUID() {
		return UID.of(this);
	}
	
	/**
	 * Champ : ID.
	 * Récupère la valeur de la propriété 'Id'.
	 * @return Long heaId <b>Obligatoire</b>
	 */
	@Field(smartType = "STyId", type = "ID", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Id")
	public Long getHeaId() {
		return heaId;
	}

	/**
	 * Champ : ID.
	 * Définit la valeur de la propriété 'Id'.
	 * @param heaId Long <b>Obligatoire</b>
	 */
	public void setHeaId(final Long heaId) {
		this.heaId = heaId;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nom'.
	 * @return String name
	 */
	@Field(smartType = "STyLabel", label = "Nom")
	public String getName() {
		return name;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nom'.
	 * @param name String
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Nom DNS/Adresse IP'.
	 * @return String dnsName
	 */
	@Field(smartType = "STyLabel", label = "Nom DNS/Adresse IP")
	public String getDnsName() {
		return dnsName;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Nom DNS/Adresse IP'.
	 * @param dnsName String
	 */
	public void setDnsName(final String dnsName) {
		this.dnsName = dnsName;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Actif'.
	 * @return Boolean active
	 */
	@Field(smartType = "STyBoolean", label = "Actif")
	public Boolean getActive() {
		return active;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Actif'.
	 * @param active Boolean
	 */
	public void setActive(final Boolean active) {
		this.active = active;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Mode Auto'.
	 * @return Boolean auto <b>Obligatoire</b>
	 */
	@Field(smartType = "STyBoolean", cardinality = io.vertigo.core.lang.Cardinality.ONE, label = "Mode Auto")
	public Boolean getAuto() {
		return auto;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Mode Auto'.
	 * @param auto Boolean <b>Obligatoire</b>
	 */
	public void setAuto(final Boolean auto) {
		this.auto = auto;
	}
	
	/**
	 * Champ : DATA.
	 * Récupère la valeur de la propriété 'Retour au mode auto'.
	 * @return Instant autoSwitch
	 */
	@Field(smartType = "STyTimestamp", label = "Retour au mode auto")
	public java.time.Instant getAutoSwitch() {
		return autoSwitch;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Retour au mode auto'.
	 * @param autoSwitch Instant
	 */
	public void setAutoSwitch(final java.time.Instant autoSwitch) {
		this.autoSwitch = autoSwitch;
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Calendrier'.
	 * @return Long wcaId <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyId", label = "Calendrier", fkDefinition = "DtWeeklyCalendar" )
	public Long getWcaId() {
		return (Long) wcaIdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Calendrier'.
	 * @param wcaId Long <b>Obligatoire</b>
	 */
	public void setWcaId(final Long wcaId) {
		wcaIdAccessor.setId(wcaId);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Protocol'.
	 * @return String proCd <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyLabel", label = "Protocol", fkDefinition = "DtProtocol" )
	public String getProCd() {
		return (String) proCdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Protocol'.
	 * @param proCd String <b>Obligatoire</b>
	 */
	public void setProCd(final String proCd) {
		proCdAccessor.setId(proCd);
	}
	
	/**
	 * Champ : FOREIGN_KEY.
	 * Récupère la valeur de la propriété 'Mode'.
	 * @return String modCd <b>Obligatoire</b>
	 */
	@io.vertigo.datamodel.structure.stereotype.ForeignKey(smartType = "STyLabel", label = "Mode", fkDefinition = "DtHeaterMode" )
	public String getModCd() {
		return (String) modCdAccessor.getId();
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Mode'.
	 * @param modCd String <b>Obligatoire</b>
	 */
	public void setModCd(final String modCd) {
		modCdAccessor.setId(modCd);
	}

 	/**
	 * Association : Mode.
	 * @return l'accesseur vers la propriété 'Mode'
	 */
	public EnumStoreVAccessor<com.mlaroche.smartheater.domain.HeaterMode, com.mlaroche.smartheater.domain.HeaterModeEnum> mode() {
		return modCdAccessor;
	}

 	/**
	 * Association : Protocol.
	 * @return l'accesseur vers la propriété 'Protocol'
	 */
	public EnumStoreVAccessor<com.mlaroche.smartheater.domain.Protocol, com.mlaroche.smartheater.domain.ProtocolEnum> protocol() {
		return proCdAccessor;
	}

 	/**
	 * Association : Calendrier.
	 * @return l'accesseur vers la propriété 'Calendrier'
	 */
	public StoreVAccessor<com.mlaroche.smartheater.domain.WeeklyCalendar> weeklyCalendar() {
		return wcaIdAccessor;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
