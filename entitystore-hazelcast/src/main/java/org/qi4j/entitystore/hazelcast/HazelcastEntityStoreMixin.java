package org.qi4j.entitystore.hazelcast;

import com.hazelcast.core.Hazelcast;
import org.qi4j.api.configuration.Configuration;
import org.qi4j.api.entity.EntityDescriptor;
import org.qi4j.api.entity.EntityReference;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.service.Activatable;
import org.qi4j.io.Input;
import org.qi4j.io.Output;
import org.qi4j.io.Receiver;
import org.qi4j.io.Sender;
import org.qi4j.spi.entitystore.EntityNotFoundException;
import org.qi4j.spi.entitystore.EntityStoreException;
import org.qi4j.spi.entitystore.helpers.MapEntityStore;

import java.io.*;
import java.util.Map;

/**
 * @author Paul Merlin <paul@nosphere.org>
 */
public class HazelcastEntityStoreMixin
        implements Activatable,
        MapEntityStore
{

   private static final String DEFAULT_MAPNAME = "qi4j-data";

   @This
   private Configuration<HazelcastConfiguration> config;
   private Map<String, String> stringMap;

   public void activate()
           throws Exception
   {
      HazelcastConfiguration configuration = config.configuration();
      String mapName = DEFAULT_MAPNAME;
      if (configuration != null && configuration.mapName() != null)
      {
         mapName = configuration.mapName().get();
      }
      stringMap = Hazelcast.getMap(mapName);
   }

   public void passivate()
           throws Exception
   {
      stringMap = null;
   }

   public Reader get(EntityReference ref)
           throws EntityStoreException
   {
      final String serializedState = stringMap.get(ref.identity());
      if (serializedState == null)
      {
         throw new EntityNotFoundException(ref);
      }
      return new StringReader(serializedState);
   }

   public void applyChanges(MapChanges changes)
           throws IOException
   {
      changes.visitMap(new MapChanger()
      {

         public Writer newEntity(final EntityReference ref, EntityDescriptor descriptor)
                 throws IOException
         {
            return new StringWriter(1000)
            {

               @Override
               public void close()
                       throws IOException
               {
                  super.close();
                  stringMap.put(ref.identity(), toString());
               }
            };
         }

         public Writer updateEntity(EntityReference ref, EntityDescriptor descriptor)
                 throws IOException
         {
            return newEntity(ref, descriptor);
         }

         public void removeEntity(EntityReference ref, EntityDescriptor descriptor)
                 throws EntityNotFoundException
         {
            stringMap.remove(ref.identity());
         }
      });
   }

   public Input<Reader, IOException> entityStates()
   {
      return new Input<Reader, IOException>()
      {
         @Override
         public <ReceiverThrowableType extends Throwable> void transferTo(Output<? super Reader, ReceiverThrowableType> output) throws IOException, ReceiverThrowableType
         {
            output.receiveFrom(new Sender<Reader, IOException>()
            {
               @Override
               public <ReceiverThrowableType extends Throwable> void sendTo(Receiver<? super Reader, ReceiverThrowableType> receiver) throws ReceiverThrowableType, IOException
               {
                  for (Map.Entry<String, String> eachEntry : stringMap.entrySet())
                  {
                     receiver.receive(new StringReader(eachEntry.getValue()));
                  }
               }
            });
         }
      };
   }
}
