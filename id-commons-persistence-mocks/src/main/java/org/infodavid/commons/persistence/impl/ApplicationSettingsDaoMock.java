package org.infodavid.commons.persistence.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.infodavid.commons.model.ApplicationSetting;
import org.infodavid.commons.persistence.dao.ApplicationSettingDao;

import com.github.javafaker.Faker;

import jakarta.persistence.PersistenceException;

/**
 * The Class ApplicationSettingsDaoMock.
 */
public class ApplicationSettingsDaoMock extends AbstractDefaultDaoMock<Long, ApplicationSetting> implements ApplicationSettingDao {

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.ApplicationSettingDao#findByScope(java.lang.String)
     */
    @Override
    public List<ApplicationSetting> findByScope(final String value) throws PersistenceException {
        final List<ApplicationSetting> results = new ArrayList<>();

        for (final Entry<Long, ApplicationSetting> entry : map.entrySet()) {
            if (StringUtils.equals(entry.getValue().getScope(), value)) {
                results.add(clone(entry.getValue()));
            }
        }

        return results;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.impl.AbstractDefaultDaoMock#getDomainClass()
     */
    @Override
    protected Class<ApplicationSetting> getDomainClass() {
        return ApplicationSetting.class;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.impl.AbstractDefaultDaoMock#getIdClass()
     */
    @Override
    protected Class<Long> getIdClass() {
        return Long.class;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.impl.AbstractDefaultDaoMock#populateEntity(com.github.javafaker.Faker, org.infodavid.commons.model.PersistentObject)
     */
    @Override
    protected ApplicationSetting populateEntity(final Faker faker, final ApplicationSetting entity) {
        super.populateEntity(faker, entity);
        entity.setLabel(faker.pokemon().name());
        entity.setScope(faker.pokemon().location());

        return entity;
    }
}
