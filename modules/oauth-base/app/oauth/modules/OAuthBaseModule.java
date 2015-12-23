package oauth.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import common.repository.HibernateRepository;
import common.repository.Repository;
import oauth.lifecycle.OnStart;
import oauth.models.*;
import oauth.services.HibernateLoggingService;
import oauth.services.LoggingService;

public class OAuthBaseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OnStart.class).asEagerSingleton();

        bind(new TypeLiteral<Repository<OAuthClient>>(){}).to(new TypeLiteral<HibernateRepository<OAuthClient>>(){});
        bind(new TypeLiteral<Repository<OAuthApi>>(){}).to(new TypeLiteral<HibernateRepository<OAuthApi>>(){});
        bind(new TypeLiteral<Repository<OAuthScope>>(){}).to(new TypeLiteral<HibernateRepository<OAuthScope>>(){});
        bind(new TypeLiteral<Repository<OAuthUrlPattern>>(){}).to(new TypeLiteral<HibernateRepository<OAuthUrlPattern>>(){});
        bind(new TypeLiteral<Repository<OAuthLog>>(){}).to(new TypeLiteral<HibernateRepository<OAuthLog>>(){});

        bind(LoggingService.class).to(HibernateLoggingService.class);
    }
}
