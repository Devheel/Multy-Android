/*
 * Copyright 2018 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.util;

import io.multy.model.entities.wallet.EthWallet;
import io.multy.model.entities.wallet.RecentAddress;
import io.multy.model.entities.wallet.Wallet;
import io.multy.model.entities.wallet.WalletAddress;
import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class MultyRealmMigration implements io.realm.RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        switch ((int) oldVersion) {
            case 1:
                final RealmObjectSchema recentAddressSchema = schema.get(RecentAddress.class.getSimpleName());
                recentAddressSchema.addField(RecentAddress.RECENT_ADDRESS_ID, long.class);
                recentAddressSchema.addPrimaryKey(RecentAddress.RECENT_ADDRESS_ID);

                final RealmObjectSchema ethWalletSchema = schema.get(EthWallet.class.getSimpleName());
                ethWalletSchema.addField(EthWallet.PENDING_BALANCE, String.class);
                oldVersion++;
            case 2:
                final RealmObjectSchema addressSchema = schema.get(WalletAddress.class.getSimpleName());
                addressSchema.removeField("amount");
                addressSchema.addField("amount", String.class);
                oldVersion++;
            case 3:
                final RealmObjectSchema walletSchema = schema.get(Wallet.class.getSimpleName());
                walletSchema.addField("syncing", boolean.class);
                oldVersion++;
            case 4:
                RealmObjectSchema contactAddressSchema = schema.create("ContactAddress");
                contactAddressSchema.addField("address", String.class, FieldAttribute.INDEXED);
                contactAddressSchema.addPrimaryKey("address");
                contactAddressSchema.addField("contactId", long.class);
                contactAddressSchema.addField("currencyId", int.class);
                contactAddressSchema.addField("networkId", int.class);
                contactAddressSchema.addField("addressCurrencyImgId", int.class);

                RealmObjectSchema contactSchema = schema.create("Contact");
                contactSchema.addField("id", long.class, FieldAttribute.INDEXED);
                contactSchema.addPrimaryKey("id");
                contactSchema.addField("name", String.class);
                contactSchema.addField("parentId", long.class);
                contactSchema.addField("photoUri", String.class);
                contactSchema.addRealmListField("addresses", contactAddressSchema);
                oldVersion++;
            case 5:
                RealmObjectSchema pendingWalletSchema = schema.get(Wallet.class.getSimpleName());
                pendingWalletSchema.addField("in", int.class);
                pendingWalletSchema.addField("out", int.class);
                oldVersion++;
        }
    }

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MultyRealmMigration);
    }

}
