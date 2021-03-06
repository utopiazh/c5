// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: wire.proto

package ohmdb.flease;

public final class Wire {
  private Wire() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface FleaseWireMessageOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // optional int64 message_id = 1;
    /**
     * <code>optional int64 message_id = 1;</code>
     *
     * <pre>
     * Envelope
     * </pre>
     */
    boolean hasMessageId();
    /**
     * <code>optional int64 message_id = 1;</code>
     *
     * <pre>
     * Envelope
     * </pre>
     */
    long getMessageId();

    // optional int64 sender_id = 2;
    /**
     * <code>optional int64 sender_id = 2;</code>
     */
    boolean hasSenderId();
    /**
     * <code>optional int64 sender_id = 2;</code>
     */
    long getSenderId();

    // optional int64 destination_id = 3;
    /**
     * <code>optional int64 destination_id = 3;</code>
     *
     * <pre>
     * The destination is implied by who received the message.
     * </pre>
     */
    boolean hasDestinationId();
    /**
     * <code>optional int64 destination_id = 3;</code>
     *
     * <pre>
     * The destination is implied by who received the message.
     * </pre>
     */
    long getDestinationId();

    // optional .ohmdb.flease.FleaseRequestMessage request_message = 4;
    /**
     * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
     *
     * <pre>
     * Payload/content
     * </pre>
     */
    boolean hasRequestMessage();
    /**
     * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
     *
     * <pre>
     * Payload/content
     * </pre>
     */
    ohmdb.flease.Flease.FleaseRequestMessage getRequestMessage();
    /**
     * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
     *
     * <pre>
     * Payload/content
     * </pre>
     */
    ohmdb.flease.Flease.FleaseRequestMessageOrBuilder getRequestMessageOrBuilder();

    // optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;
    /**
     * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
     */
    boolean hasReplyMessage();
    /**
     * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
     */
    ohmdb.flease.Flease.FleaseReplyMessage getReplyMessage();
    /**
     * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
     */
    ohmdb.flease.Flease.FleaseReplyMessageOrBuilder getReplyMessageOrBuilder();
  }
  /**
   * Protobuf type {@code ohmdb.flease.FleaseWireMessage}
   */
  public static final class FleaseWireMessage extends
      com.google.protobuf.GeneratedMessage
      implements FleaseWireMessageOrBuilder {
    // Use FleaseWireMessage.newBuilder() to construct.
    private FleaseWireMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private FleaseWireMessage(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final FleaseWireMessage defaultInstance;
    public static FleaseWireMessage getDefaultInstance() {
      return defaultInstance;
    }

    public FleaseWireMessage getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private FleaseWireMessage(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              messageId_ = input.readInt64();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              senderId_ = input.readInt64();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              destinationId_ = input.readInt64();
              break;
            }
            case 34: {
              ohmdb.flease.Flease.FleaseRequestMessage.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = requestMessage_.toBuilder();
              }
              requestMessage_ = input.readMessage(ohmdb.flease.Flease.FleaseRequestMessage.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(requestMessage_);
                requestMessage_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
            case 42: {
              ohmdb.flease.Flease.FleaseReplyMessage.Builder subBuilder = null;
              if (((bitField0_ & 0x00000010) == 0x00000010)) {
                subBuilder = replyMessage_.toBuilder();
              }
              replyMessage_ = input.readMessage(ohmdb.flease.Flease.FleaseReplyMessage.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(replyMessage_);
                replyMessage_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000010;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ohmdb.flease.Wire.internal_static_ohmdb_flease_FleaseWireMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ohmdb.flease.Wire.internal_static_ohmdb_flease_FleaseWireMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ohmdb.flease.Wire.FleaseWireMessage.class, ohmdb.flease.Wire.FleaseWireMessage.Builder.class);
    }

    public static com.google.protobuf.Parser<FleaseWireMessage> PARSER =
        new com.google.protobuf.AbstractParser<FleaseWireMessage>() {
      public FleaseWireMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new FleaseWireMessage(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<FleaseWireMessage> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // optional int64 message_id = 1;
    public static final int MESSAGE_ID_FIELD_NUMBER = 1;
    private long messageId_;
    /**
     * <code>optional int64 message_id = 1;</code>
     *
     * <pre>
     * Envelope
     * </pre>
     */
    public boolean hasMessageId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional int64 message_id = 1;</code>
     *
     * <pre>
     * Envelope
     * </pre>
     */
    public long getMessageId() {
      return messageId_;
    }

    // optional int64 sender_id = 2;
    public static final int SENDER_ID_FIELD_NUMBER = 2;
    private long senderId_;
    /**
     * <code>optional int64 sender_id = 2;</code>
     */
    public boolean hasSenderId() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int64 sender_id = 2;</code>
     */
    public long getSenderId() {
      return senderId_;
    }

    // optional int64 destination_id = 3;
    public static final int DESTINATION_ID_FIELD_NUMBER = 3;
    private long destinationId_;
    /**
     * <code>optional int64 destination_id = 3;</code>
     *
     * <pre>
     * The destination is implied by who received the message.
     * </pre>
     */
    public boolean hasDestinationId() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional int64 destination_id = 3;</code>
     *
     * <pre>
     * The destination is implied by who received the message.
     * </pre>
     */
    public long getDestinationId() {
      return destinationId_;
    }

    // optional .ohmdb.flease.FleaseRequestMessage request_message = 4;
    public static final int REQUEST_MESSAGE_FIELD_NUMBER = 4;
    private ohmdb.flease.Flease.FleaseRequestMessage requestMessage_;
    /**
     * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
     *
     * <pre>
     * Payload/content
     * </pre>
     */
    public boolean hasRequestMessage() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
     *
     * <pre>
     * Payload/content
     * </pre>
     */
    public ohmdb.flease.Flease.FleaseRequestMessage getRequestMessage() {
      return requestMessage_;
    }
    /**
     * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
     *
     * <pre>
     * Payload/content
     * </pre>
     */
    public ohmdb.flease.Flease.FleaseRequestMessageOrBuilder getRequestMessageOrBuilder() {
      return requestMessage_;
    }

    // optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;
    public static final int REPLY_MESSAGE_FIELD_NUMBER = 5;
    private ohmdb.flease.Flease.FleaseReplyMessage replyMessage_;
    /**
     * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
     */
    public boolean hasReplyMessage() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
     */
    public ohmdb.flease.Flease.FleaseReplyMessage getReplyMessage() {
      return replyMessage_;
    }
    /**
     * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
     */
    public ohmdb.flease.Flease.FleaseReplyMessageOrBuilder getReplyMessageOrBuilder() {
      return replyMessage_;
    }

    private void initFields() {
      messageId_ = 0L;
      senderId_ = 0L;
      destinationId_ = 0L;
      requestMessage_ = ohmdb.flease.Flease.FleaseRequestMessage.getDefaultInstance();
      replyMessage_ = ohmdb.flease.Flease.FleaseReplyMessage.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt64(1, messageId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt64(2, senderId_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt64(3, destinationId_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(4, requestMessage_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeMessage(5, replyMessage_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, messageId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, senderId_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(3, destinationId_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, requestMessage_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, replyMessage_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static ohmdb.flease.Wire.FleaseWireMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ohmdb.flease.Wire.FleaseWireMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ohmdb.flease.Wire.FleaseWireMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ohmdb.flease.Wire.FleaseWireMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ohmdb.flease.Wire.FleaseWireMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ohmdb.flease.Wire.FleaseWireMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static ohmdb.flease.Wire.FleaseWireMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static ohmdb.flease.Wire.FleaseWireMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static ohmdb.flease.Wire.FleaseWireMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static ohmdb.flease.Wire.FleaseWireMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ohmdb.flease.Wire.FleaseWireMessage prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ohmdb.flease.FleaseWireMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements ohmdb.flease.Wire.FleaseWireMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ohmdb.flease.Wire.internal_static_ohmdb_flease_FleaseWireMessage_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ohmdb.flease.Wire.internal_static_ohmdb_flease_FleaseWireMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                ohmdb.flease.Wire.FleaseWireMessage.class, ohmdb.flease.Wire.FleaseWireMessage.Builder.class);
      }

      // Construct using ohmdb.flease.Wire.FleaseWireMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getRequestMessageFieldBuilder();
          getReplyMessageFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        messageId_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        senderId_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        destinationId_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000004);
        if (requestMessageBuilder_ == null) {
          requestMessage_ = ohmdb.flease.Flease.FleaseRequestMessage.getDefaultInstance();
        } else {
          requestMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        if (replyMessageBuilder_ == null) {
          replyMessage_ = ohmdb.flease.Flease.FleaseReplyMessage.getDefaultInstance();
        } else {
          replyMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ohmdb.flease.Wire.internal_static_ohmdb_flease_FleaseWireMessage_descriptor;
      }

      public ohmdb.flease.Wire.FleaseWireMessage getDefaultInstanceForType() {
        return ohmdb.flease.Wire.FleaseWireMessage.getDefaultInstance();
      }

      public ohmdb.flease.Wire.FleaseWireMessage build() {
        ohmdb.flease.Wire.FleaseWireMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public ohmdb.flease.Wire.FleaseWireMessage buildPartial() {
        ohmdb.flease.Wire.FleaseWireMessage result = new ohmdb.flease.Wire.FleaseWireMessage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.messageId_ = messageId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.senderId_ = senderId_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.destinationId_ = destinationId_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (requestMessageBuilder_ == null) {
          result.requestMessage_ = requestMessage_;
        } else {
          result.requestMessage_ = requestMessageBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        if (replyMessageBuilder_ == null) {
          result.replyMessage_ = replyMessage_;
        } else {
          result.replyMessage_ = replyMessageBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ohmdb.flease.Wire.FleaseWireMessage) {
          return mergeFrom((ohmdb.flease.Wire.FleaseWireMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ohmdb.flease.Wire.FleaseWireMessage other) {
        if (other == ohmdb.flease.Wire.FleaseWireMessage.getDefaultInstance()) return this;
        if (other.hasMessageId()) {
          setMessageId(other.getMessageId());
        }
        if (other.hasSenderId()) {
          setSenderId(other.getSenderId());
        }
        if (other.hasDestinationId()) {
          setDestinationId(other.getDestinationId());
        }
        if (other.hasRequestMessage()) {
          mergeRequestMessage(other.getRequestMessage());
        }
        if (other.hasReplyMessage()) {
          mergeReplyMessage(other.getReplyMessage());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ohmdb.flease.Wire.FleaseWireMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ohmdb.flease.Wire.FleaseWireMessage) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // optional int64 message_id = 1;
      private long messageId_ ;
      /**
       * <code>optional int64 message_id = 1;</code>
       *
       * <pre>
       * Envelope
       * </pre>
       */
      public boolean hasMessageId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional int64 message_id = 1;</code>
       *
       * <pre>
       * Envelope
       * </pre>
       */
      public long getMessageId() {
        return messageId_;
      }
      /**
       * <code>optional int64 message_id = 1;</code>
       *
       * <pre>
       * Envelope
       * </pre>
       */
      public Builder setMessageId(long value) {
        bitField0_ |= 0x00000001;
        messageId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int64 message_id = 1;</code>
       *
       * <pre>
       * Envelope
       * </pre>
       */
      public Builder clearMessageId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        messageId_ = 0L;
        onChanged();
        return this;
      }

      // optional int64 sender_id = 2;
      private long senderId_ ;
      /**
       * <code>optional int64 sender_id = 2;</code>
       */
      public boolean hasSenderId() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional int64 sender_id = 2;</code>
       */
      public long getSenderId() {
        return senderId_;
      }
      /**
       * <code>optional int64 sender_id = 2;</code>
       */
      public Builder setSenderId(long value) {
        bitField0_ |= 0x00000002;
        senderId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int64 sender_id = 2;</code>
       */
      public Builder clearSenderId() {
        bitField0_ = (bitField0_ & ~0x00000002);
        senderId_ = 0L;
        onChanged();
        return this;
      }

      // optional int64 destination_id = 3;
      private long destinationId_ ;
      /**
       * <code>optional int64 destination_id = 3;</code>
       *
       * <pre>
       * The destination is implied by who received the message.
       * </pre>
       */
      public boolean hasDestinationId() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional int64 destination_id = 3;</code>
       *
       * <pre>
       * The destination is implied by who received the message.
       * </pre>
       */
      public long getDestinationId() {
        return destinationId_;
      }
      /**
       * <code>optional int64 destination_id = 3;</code>
       *
       * <pre>
       * The destination is implied by who received the message.
       * </pre>
       */
      public Builder setDestinationId(long value) {
        bitField0_ |= 0x00000004;
        destinationId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int64 destination_id = 3;</code>
       *
       * <pre>
       * The destination is implied by who received the message.
       * </pre>
       */
      public Builder clearDestinationId() {
        bitField0_ = (bitField0_ & ~0x00000004);
        destinationId_ = 0L;
        onChanged();
        return this;
      }

      // optional .ohmdb.flease.FleaseRequestMessage request_message = 4;
      private ohmdb.flease.Flease.FleaseRequestMessage requestMessage_ = ohmdb.flease.Flease.FleaseRequestMessage.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          ohmdb.flease.Flease.FleaseRequestMessage, ohmdb.flease.Flease.FleaseRequestMessage.Builder, ohmdb.flease.Flease.FleaseRequestMessageOrBuilder> requestMessageBuilder_;
      /**
       * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
       *
       * <pre>
       * Payload/content
       * </pre>
       */
      public boolean hasRequestMessage() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
       *
       * <pre>
       * Payload/content
       * </pre>
       */
      public ohmdb.flease.Flease.FleaseRequestMessage getRequestMessage() {
        if (requestMessageBuilder_ == null) {
          return requestMessage_;
        } else {
          return requestMessageBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
       *
       * <pre>
       * Payload/content
       * </pre>
       */
      public Builder setRequestMessage(ohmdb.flease.Flease.FleaseRequestMessage value) {
        if (requestMessageBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          requestMessage_ = value;
          onChanged();
        } else {
          requestMessageBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
       *
       * <pre>
       * Payload/content
       * </pre>
       */
      public Builder setRequestMessage(
          ohmdb.flease.Flease.FleaseRequestMessage.Builder builderForValue) {
        if (requestMessageBuilder_ == null) {
          requestMessage_ = builderForValue.build();
          onChanged();
        } else {
          requestMessageBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
       *
       * <pre>
       * Payload/content
       * </pre>
       */
      public Builder mergeRequestMessage(ohmdb.flease.Flease.FleaseRequestMessage value) {
        if (requestMessageBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              requestMessage_ != ohmdb.flease.Flease.FleaseRequestMessage.getDefaultInstance()) {
            requestMessage_ =
              ohmdb.flease.Flease.FleaseRequestMessage.newBuilder(requestMessage_).mergeFrom(value).buildPartial();
          } else {
            requestMessage_ = value;
          }
          onChanged();
        } else {
          requestMessageBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      /**
       * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
       *
       * <pre>
       * Payload/content
       * </pre>
       */
      public Builder clearRequestMessage() {
        if (requestMessageBuilder_ == null) {
          requestMessage_ = ohmdb.flease.Flease.FleaseRequestMessage.getDefaultInstance();
          onChanged();
        } else {
          requestMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      /**
       * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
       *
       * <pre>
       * Payload/content
       * </pre>
       */
      public ohmdb.flease.Flease.FleaseRequestMessage.Builder getRequestMessageBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getRequestMessageFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
       *
       * <pre>
       * Payload/content
       * </pre>
       */
      public ohmdb.flease.Flease.FleaseRequestMessageOrBuilder getRequestMessageOrBuilder() {
        if (requestMessageBuilder_ != null) {
          return requestMessageBuilder_.getMessageOrBuilder();
        } else {
          return requestMessage_;
        }
      }
      /**
       * <code>optional .ohmdb.flease.FleaseRequestMessage request_message = 4;</code>
       *
       * <pre>
       * Payload/content
       * </pre>
       */
      private com.google.protobuf.SingleFieldBuilder<
          ohmdb.flease.Flease.FleaseRequestMessage, ohmdb.flease.Flease.FleaseRequestMessage.Builder, ohmdb.flease.Flease.FleaseRequestMessageOrBuilder> 
          getRequestMessageFieldBuilder() {
        if (requestMessageBuilder_ == null) {
          requestMessageBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              ohmdb.flease.Flease.FleaseRequestMessage, ohmdb.flease.Flease.FleaseRequestMessage.Builder, ohmdb.flease.Flease.FleaseRequestMessageOrBuilder>(
                  requestMessage_,
                  getParentForChildren(),
                  isClean());
          requestMessage_ = null;
        }
        return requestMessageBuilder_;
      }

      // optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;
      private ohmdb.flease.Flease.FleaseReplyMessage replyMessage_ = ohmdb.flease.Flease.FleaseReplyMessage.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          ohmdb.flease.Flease.FleaseReplyMessage, ohmdb.flease.Flease.FleaseReplyMessage.Builder, ohmdb.flease.Flease.FleaseReplyMessageOrBuilder> replyMessageBuilder_;
      /**
       * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
       */
      public boolean hasReplyMessage() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
       */
      public ohmdb.flease.Flease.FleaseReplyMessage getReplyMessage() {
        if (replyMessageBuilder_ == null) {
          return replyMessage_;
        } else {
          return replyMessageBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
       */
      public Builder setReplyMessage(ohmdb.flease.Flease.FleaseReplyMessage value) {
        if (replyMessageBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          replyMessage_ = value;
          onChanged();
        } else {
          replyMessageBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
       */
      public Builder setReplyMessage(
          ohmdb.flease.Flease.FleaseReplyMessage.Builder builderForValue) {
        if (replyMessageBuilder_ == null) {
          replyMessage_ = builderForValue.build();
          onChanged();
        } else {
          replyMessageBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
       */
      public Builder mergeReplyMessage(ohmdb.flease.Flease.FleaseReplyMessage value) {
        if (replyMessageBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010) &&
              replyMessage_ != ohmdb.flease.Flease.FleaseReplyMessage.getDefaultInstance()) {
            replyMessage_ =
              ohmdb.flease.Flease.FleaseReplyMessage.newBuilder(replyMessage_).mergeFrom(value).buildPartial();
          } else {
            replyMessage_ = value;
          }
          onChanged();
        } else {
          replyMessageBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000010;
        return this;
      }
      /**
       * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
       */
      public Builder clearReplyMessage() {
        if (replyMessageBuilder_ == null) {
          replyMessage_ = ohmdb.flease.Flease.FleaseReplyMessage.getDefaultInstance();
          onChanged();
        } else {
          replyMessageBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      /**
       * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
       */
      public ohmdb.flease.Flease.FleaseReplyMessage.Builder getReplyMessageBuilder() {
        bitField0_ |= 0x00000010;
        onChanged();
        return getReplyMessageFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
       */
      public ohmdb.flease.Flease.FleaseReplyMessageOrBuilder getReplyMessageOrBuilder() {
        if (replyMessageBuilder_ != null) {
          return replyMessageBuilder_.getMessageOrBuilder();
        } else {
          return replyMessage_;
        }
      }
      /**
       * <code>optional .ohmdb.flease.FleaseReplyMessage reply_message = 5;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          ohmdb.flease.Flease.FleaseReplyMessage, ohmdb.flease.Flease.FleaseReplyMessage.Builder, ohmdb.flease.Flease.FleaseReplyMessageOrBuilder> 
          getReplyMessageFieldBuilder() {
        if (replyMessageBuilder_ == null) {
          replyMessageBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              ohmdb.flease.Flease.FleaseReplyMessage, ohmdb.flease.Flease.FleaseReplyMessage.Builder, ohmdb.flease.Flease.FleaseReplyMessageOrBuilder>(
                  replyMessage_,
                  getParentForChildren(),
                  isClean());
          replyMessage_ = null;
        }
        return replyMessageBuilder_;
      }

      // @@protoc_insertion_point(builder_scope:ohmdb.flease.FleaseWireMessage)
    }

    static {
      defaultInstance = new FleaseWireMessage(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:ohmdb.flease.FleaseWireMessage)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ohmdb_flease_FleaseWireMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ohmdb_flease_FleaseWireMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\nwire.proto\022\014ohmdb.flease\032\014flease.proto" +
      "\"\310\001\n\021FleaseWireMessage\022\022\n\nmessage_id\030\001 \001" +
      "(\003\022\021\n\tsender_id\030\002 \001(\003\022\026\n\016destination_id\030" +
      "\003 \001(\003\022;\n\017request_message\030\004 \001(\0132\".ohmdb.f" +
      "lease.FleaseRequestMessage\0227\n\rreply_mess" +
      "age\030\005 \001(\0132 .ohmdb.flease.FleaseReplyMess" +
      "ageB\020\n\014ohmdb.fleaseH\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_ohmdb_flease_FleaseWireMessage_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_ohmdb_flease_FleaseWireMessage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ohmdb_flease_FleaseWireMessage_descriptor,
              new java.lang.String[] { "MessageId", "SenderId", "DestinationId", "RequestMessage", "ReplyMessage", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          ohmdb.flease.Flease.getDescriptor(),
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
