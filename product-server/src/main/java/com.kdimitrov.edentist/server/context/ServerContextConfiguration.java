/*
 * Copyright (c) 2020 by Axway Software
 * All brand or product names are trademarks or registered trademarks of their respective holders. This document and
 * the software described in this document are the property of Axway Software and are protected as Axway Software
 * trade secrets. No part of this work may be reproduced or disseminated in any form or by any means, without the
 * prior written permission of Axway Software.
 */

package com.kdimitrov.edentist.server.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ServerContext.class)
public class ServerContextConfiguration {
}
