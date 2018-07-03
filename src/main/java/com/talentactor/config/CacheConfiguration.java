package com.talentactor.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.talentactor.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.talentactor.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.talentactor.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".roles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".films", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".televisions", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".internets", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".commercials", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".prints", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".theaters", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".voices", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".skills", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".sports", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".swimmings", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".combats", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".languages", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".instruments", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".weapons", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".cyclings", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".circuses", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Profile.class.getName() + ".horses", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Skill.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Skill.class.getName() + ".profiles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Film.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Television.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Internet.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Commercial.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Print.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Theater.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Voice.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Project.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Project.class.getName() + ".roles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Role.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Sport.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Sport.class.getName() + ".profiles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Swimming.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Swimming.class.getName() + ".profiles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Combat.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Combat.class.getName() + ".profiles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Language.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Language.class.getName() + ".profiles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Instrument.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Instrument.class.getName() + ".profiles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Weapon.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Weapon.class.getName() + ".profiles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Cycling.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Cycling.class.getName() + ".profiles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Circus.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Circus.class.getName() + ".profiles", jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Horse.class.getName(), jcacheConfiguration);
            cm.createCache(com.talentactor.domain.Horse.class.getName() + ".profiles", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
