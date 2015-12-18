package oauth.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import common.repository.HibernateRepository;
import common.repository.Repository;
import oauth.models.OAuthApi;
import oauth.models.OAuthClient;
import oauth.models.OAuthLevel;
import oauth.models.OAuthScope;

public class OAuthBaseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<Repository<OAuthClient>>(){}).to(new TypeLiteral<HibernateRepository<OAuthClient>>(){});
        bind(new TypeLiteral<Repository<OAuthApi>>(){}).to(new TypeLiteral<HibernateRepository<OAuthApi>>(){});
        bind(new TypeLiteral<Repository<OAuthLevel>>(){}).to(new TypeLiteral<HibernateRepository<OAuthLevel>>(){});
        bind(new TypeLiteral<Repository<OAuthScope>>(){}).to(new TypeLiteral<HibernateRepository<OAuthScope>>(){});
    }
}
