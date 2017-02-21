package com.example.islam.etbara3.Model;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;

public class Model
{
  private String organizationMouny;
  private String ownerId;
  private Integer organizationID;
  private java.util.Date created;
  private String organizationPhone;
  private String organizationName;
  private String objectId;
  private String organizationYoutubeLink;
  private java.util.Date updated;
  private String organizationAccountNo;
  private String organozationService;
  private String organizationYoutubeName;
  private String organizationInfo;
  private String organizationSMSContent;
  private byte[] organizationPhoto;
  private String organizationSMS;
  public String getOrganizationMouny()
  {
    return organizationMouny;
  }

  public void setOrganizationMouny( String organizationMouny )
  {
    this.organizationMouny = organizationMouny;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Integer getOrganizationID()
  {
    return organizationID;
  }

  public void setOrganizationID( Integer organizationID )
  {
    this.organizationID = organizationID;
  }

  public java.util.Date getCreated()
  {
    return created;
  }

  public String getOrganizationPhone()
  {
    return organizationPhone;
  }

  public void setOrganizationPhone( String organizationPhone )
  {
    this.organizationPhone = organizationPhone;
  }

  public String getOrganizationName()
  {
    return organizationName;
  }

  public void setOrganizationName( String organizationName )
  {
    this.organizationName = organizationName;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getOrganizationYoutubeLink()
  {
    return organizationYoutubeLink;
  }

  public void setOrganizationYoutubeLink( String organizationYoutubeLink )
  {
    this.organizationYoutubeLink = organizationYoutubeLink;
  }

  public java.util.Date getUpdated()
  {
    return updated;
  }

  public String getOrganizationAccountNo()
  {
    return organizationAccountNo;
  }

  public void setOrganizationAccountNo( String organizationAccountNo )
  {
    this.organizationAccountNo = organizationAccountNo;
  }

  public String getOrganozationService()
  {
    return organozationService;
  }

  public void setOrganozationService( String organozationService )
  {
    this.organozationService = organozationService;
  }

  public String getOrganizationYoutubeName()
  {
    return organizationYoutubeName;
  }

  public void setOrganizationYoutubeName( String organizationYoutubeName )
  {
    this.organizationYoutubeName = organizationYoutubeName;
  }

  public String getOrganizationInfo()
  {
    return organizationInfo;
  }

  public void setOrganizationInfo( String organizationInfo )
  {
    this.organizationInfo = organizationInfo;
  }

  public String getOrganizationSMSContent()
  {
    return organizationSMSContent;
  }

  public void setOrganizationSMSContent( String organizationSMSContent )
  {
    this.organizationSMSContent = organizationSMSContent;
  }

  public byte[] getOrganizationPhoto()
  {
    return organizationPhoto;
  }

  public void setOrganizationPhoto( byte[] organizationPhoto )
  {
    this.organizationPhoto = organizationPhoto;
  }

  public String getOrganizationSMS()
  {
    return organizationSMS;
  }

  public void setOrganizationSMS( String organizationSMS )
  {
    this.organizationSMS = organizationSMS;
  }

                                                    
  public Model save()
  {
    return Backendless.Data.of( Model.class ).save( this );
  }

  public Future<Model> saveAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Model> future = new Future<Model>();
      Backendless.Data.of( Model.class ).save( this, future );

      return future;
    }
  }

  public void saveAsync( AsyncCallback<Model> callback )
  {
    Backendless.Data.of( Model.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Model.class ).remove( this );
  }

  public Future<Long> removeAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Long> future = new Future<Long>();
      Backendless.Data.of( Model.class ).remove( this, future );

      return future;
    }
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Model.class ).remove( this, callback );
  }

  public static Model findById( String id )
  {
    return Backendless.Data.of( Model.class ).findById( id );
  }

  public static Future<Model> findByIdAsync( String id )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Model> future = new Future<Model>();
      Backendless.Data.of( Model.class ).findById( id, future );

      return future;
    }
  }

  public static void findByIdAsync( String id, AsyncCallback<Model> callback )
  {
    Backendless.Data.of( Model.class ).findById( id, callback );
  }

  public static Model findFirst()
  {
    return Backendless.Data.of( Model.class ).findFirst();
  }

  public static Future<Model> findFirstAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Model> future = new Future<Model>();
      Backendless.Data.of( Model.class ).findFirst( future );

      return future;
    }
  }

  public static void findFirstAsync( AsyncCallback<Model> callback )
  {
    Backendless.Data.of( Model.class ).findFirst( callback );
  }

  public static Model findLast()
  {
    return Backendless.Data.of( Model.class ).findLast();
  }

  public static Future<Model> findLastAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Model> future = new Future<Model>();
      Backendless.Data.of( Model.class ).findLast( future );

      return future;
    }
  }

  public static void findLastAsync( AsyncCallback<Model> callback )
  {
    Backendless.Data.of( Model.class ).findLast( callback );
  }

  public static BackendlessCollection<Model> find( BackendlessDataQuery query )
  {
    return Backendless.Data.of( Model.class ).find( query );
  }

  public static Future<BackendlessCollection<Model>> findAsync( BackendlessDataQuery query )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<BackendlessCollection<Model>> future = new Future<BackendlessCollection<Model>>();
      Backendless.Data.of( Model.class ).find( query, future );

      return future;
    }
  }

  public static void findAsync( BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Model>> callback )
  {
    Backendless.Data.of( Model.class ).find( query, callback );
  }
}