<?xml version="1.0" encoding="big5"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="big5" indent="yes" />
	<xsl:template match="/">
		<CInTxBody>
			<xsl:for-each select="/Tx/TxBody/*">
				<xsl:if test="name()!='TxRepeat'">
					<Field>
						<Name>
							<xsl:value-of select="name()" />
						</Name>
						<Value>
							<xsl:value-of select="." />
						</Value>
					</Field>
				</xsl:if>
				<xsl:if test="name() = 'TxRepeat'">
					<TxRepeat>
						<xsl:for-each select="*">
							<Field>
								<Name>
									<xsl:value-of select="name()" />
								</Name>
								<Value>
									<xsl:value-of select="." />
								</Value>
							</Field>
						</xsl:for-each>
					</TxRepeat>
				</xsl:if>
			</xsl:for-each>
		</CInTxBody>
	</xsl:template>
</xsl:stylesheet>