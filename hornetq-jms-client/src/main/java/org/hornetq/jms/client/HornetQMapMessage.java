/*
 * Copyright 2005-2014 Red Hat, Inc.
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.hornetq.jms.client;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageFormatException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.HornetQPropertyConversionException;
import org.hornetq.api.core.Message;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.utils.TypedProperties;


import static org.hornetq.reader.MapMessageUtil.writeBodyMap;
import static org.hornetq.reader.MapMessageUtil.readBodyMap;

/**
 * HornetQ implementation of a JMS MapMessage.
 *
 * @author Norbert Lataille (Norbert.Lataille@m4x.org)
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 * @author <a href="mailto:ataylor@redhat.com">Andy Taylor</a>
 * @version $Revision: 3412 $
 */
public final class HornetQMapMessage extends HornetQMessage implements MapMessage
{
   // Constants -----------------------------------------------------

   public static final byte TYPE = Message.MAP_TYPE;

   // Attributes ----------------------------------------------------

   private final TypedProperties map = new TypedProperties();

   private boolean invalid;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   /*
    * This constructor is used to construct messages prior to sending
    */
   protected HornetQMapMessage(final ClientSession session)
   {
      super(HornetQMapMessage.TYPE, session);

      invalid = true;
   }

   /*
    * This constructor is used during reading
    */
   protected HornetQMapMessage(final ClientMessage message, final ClientSession session)
   {
      super(message, session);

      invalid = false;
   }

   public HornetQMapMessage()
   {
      invalid = false;
   }

   /**
    * Constructor for a foreign MapMessage
    *
    * @param foreign
    * @throws JMSException
    */
   public HornetQMapMessage(final MapMessage foreign, final ClientSession session) throws JMSException
   {
      super(foreign, HornetQMapMessage.TYPE, session);
      Enumeration<?> names = foreign.getMapNames();
      while (names.hasMoreElements())
      {
         String name = (String) names.nextElement();
         Object obj = foreign.getObject(name);
         setObject(name, obj);
      }
   }

   // Public --------------------------------------------------------

   @Override
   public byte getType()
   {
      return HornetQMapMessage.TYPE;
   }

   // MapMessage implementation -------------------------------------

   public void setBoolean(final String name, final boolean value) throws JMSException
   {
      checkName(name);
      map.putBooleanProperty(new SimpleString(name), value);
      invalid = true;
   }

   public void setByte(final String name, final byte value) throws JMSException
   {
      checkName(name);
      map.putByteProperty(new SimpleString(name), value);
      invalid = true;
   }

   public void setShort(final String name, final short value) throws JMSException
   {
      checkName(name);
      map.putShortProperty(new SimpleString(name), value);
      invalid = true;
   }

   public void setChar(final String name, final char value) throws JMSException
   {
      checkName(name);
      map.putCharProperty(new SimpleString(name), value);
      invalid = true;
   }

   public void setInt(final String name, final int value) throws JMSException
   {
      checkName(name);
      map.putIntProperty(new SimpleString(name), value);
      invalid = true;
   }

   public void setLong(final String name, final long value) throws JMSException
   {
      checkName(name);
      map.putLongProperty(new SimpleString(name), value);
      invalid = true;
   }

   public void setFloat(final String name, final float value) throws JMSException
   {
      checkName(name);
      map.putFloatProperty(new SimpleString(name), value);
      invalid = true;
   }

   public void setDouble(final String name, final double value) throws JMSException
   {
      checkName(name);
      map.putDoubleProperty(new SimpleString(name), value);
      invalid = true;
   }

   public void setString(final String name, final String value) throws JMSException
   {
      checkName(name);
      map.putSimpleStringProperty(new SimpleString(name), value == null ? null : new SimpleString(value));
      invalid = true;
   }

   public void setBytes(final String name, final byte[] value) throws JMSException
   {
      checkName(name);
      map.putBytesProperty(new SimpleString(name), value);
      invalid = true;
   }

   public void setBytes(final String name, final byte[] value, final int offset, final int length) throws JMSException
   {
      checkName(name);
      if (offset + length > value.length)
      {
         throw new JMSException("Invalid offset/length");
      }
      byte[] newBytes = new byte[length];
      System.arraycopy(value, offset, newBytes, 0, length);
      map.putBytesProperty(new SimpleString(name), newBytes);
      invalid = true;
   }

   public void setObject(final String name, final Object value) throws JMSException
   {
      checkName(name);
      try
      {
         TypedProperties.setObjectProperty(new SimpleString(name), value, map);
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
      invalid = true;
   }

   public boolean getBoolean(final String name) throws JMSException
   {
      try
      {
         return map.getBooleanProperty(new SimpleString(name));
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
   }

   public byte getByte(final String name) throws JMSException
   {
      try
      {
         return map.getByteProperty(new SimpleString(name));
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
   }

   public short getShort(final String name) throws JMSException
   {
      try
      {
         return map.getShortProperty(new SimpleString(name));
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
   }

   public char getChar(final String name) throws JMSException
   {
      try
      {
         return map.getCharProperty(new SimpleString(name));
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
   }

   public int getInt(final String name) throws JMSException
   {
      try
      {
         return map.getIntProperty(new SimpleString(name));
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
   }

   public long getLong(final String name) throws JMSException
   {
      try
      {
         return map.getLongProperty(new SimpleString(name));
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
   }

   public float getFloat(final String name) throws JMSException
   {
      try
      {
         return map.getFloatProperty(new SimpleString(name));
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
   }

   public double getDouble(final String name) throws JMSException
   {
      try
      {
         return map.getDoubleProperty(new SimpleString(name));
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
   }

   public String getString(final String name) throws JMSException
   {
      try
      {
         SimpleString str = map.getSimpleStringProperty(new SimpleString(name));
         if (str == null)
         {
            return null;
         }
         else
         {
            return str.toString();
         }
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
   }

   public byte[] getBytes(final String name) throws JMSException
   {
      try
      {
         return map.getBytesProperty(new SimpleString(name));
      }
      catch (HornetQPropertyConversionException e)
      {
         throw new MessageFormatException(e.getMessage());
      }
   }

   public Object getObject(final String name) throws JMSException
   {
      Object val = map.getProperty(new SimpleString(name));

      if (val instanceof SimpleString)
      {
         val = ((SimpleString) val).toString();
      }

      return val;
   }

   public Enumeration getMapNames() throws JMSException
   {
      Set<SimpleString> simplePropNames = map.getPropertyNames();
      Set<String> propNames = new HashSet<String>(simplePropNames.size());

      for (SimpleString str : simplePropNames)
      {
         propNames.add(str.toString());
      }

      return Collections.enumeration(propNames);
   }

   public boolean itemExists(final String name) throws JMSException
   {
      return map.containsProperty(new SimpleString(name));
   }


   // HornetQRAMessage overrides ----------------------------------------

   @Override
   public void clearBody() throws JMSException
   {
      super.clearBody();

      map.clear();

      invalid = true;
   }

   @Override
   public void doBeforeSend() throws Exception
   {
      if (invalid)
      {
         writeBodyMap(message, map);
         invalid = false;
      }

      super.doBeforeSend();
   }

   @Override
   public void doBeforeReceive() throws HornetQException
   {
      super.doBeforeReceive();

      readBodyMap(message, map);
   }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   /**
    * Check the name
    *
    * @param name the name
    */
   private void checkName(final String name) throws JMSException
   {
      checkWrite();

      if (name == null)
      {
         throw HornetQJMSClientBundle.BUNDLE.nameCannotBeNull();
      }
      if (name.equals(""))
      {
         throw HornetQJMSClientBundle.BUNDLE.nameCannotBeEmpty();
      }
   }

   @Override
   protected boolean hasNoBody()
   {
      return map.isEmpty();
   }

   @Override
   public boolean isBodyAssignableTo(@SuppressWarnings("rawtypes")
                                     Class c)
   {
      if (hasNoBody())
      {
         return true;
      }
      return c.isAssignableFrom(java.util.Map.class);
   }

   @SuppressWarnings("unchecked")
   @Override
   protected <T> T getBodyInternal(Class<T> c)
   {
      return (T) map.getMap();
   }
}
