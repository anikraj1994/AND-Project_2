package me.anikraj.popularmoviespart1;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by anik on 12/02/16.
 */
@SimpleSQLConfig(
        name = "FavoriteProvider",
        authority = "me.anikraj.popularmovies.authority",
        database = "base",
        version = 1)
public class FavoriteProviderConfig implements ProviderConfig {
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}